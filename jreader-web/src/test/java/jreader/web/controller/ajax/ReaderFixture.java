package jreader.web.controller.ajax;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedEntryDto;
import jreader.dto.FeedEntryDto;
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.RssService;
import jreader.services.UserService;
import jreader.web.controller.TaskController;
import jreader.web.controller.ajax.dto.ArchivedEntry;
import jreader.web.controller.ajax.dto.Entry;
import jreader.web.controller.ajax.dto.Subscription;
import jreader.web.test.AbstractDataStoreTest;
import jreader.web.test.FeedRegistry;

public abstract class ReaderFixture extends AbstractDataStoreTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    @Autowired
    private GroupController groupController;
    @Autowired
    private SubscriptionController subscriptionController;
    @Autowired
    private EntryController entryController;
    @Autowired
    private ArchiveController archiveController;
    @Autowired
    private ArchivedEntryController archivedEntryController;
    @Autowired
    private TaskController taskController;
    
    @Autowired
    private UserService userService;
    @Autowired
    @InjectMocks
    private RssService rssService;
    @Autowired
    @InjectMocks
    private CronService cronService;

    @Mock
    private Principal principal;
    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private DateHelper dateHelper;
    
    private FeedRegistry feedRegistry = new FeedRegistry();
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    public void initUser(String username) {
        userService.register(username);
        when(principal.getName()).thenReturn(username);
    }
    
    public void createFeed(String title, String url) throws Exception {
        SyndFeed feed = feedRegistry.registerFeed(title, url);
        when(feedFetcher.retrieveFeed(new URL(url))).thenReturn(feed);
    }
    
    public void createEntry(String feedTitle, String title, String description, String author, String link, String publishedDate) throws ParseException {
        feedRegistry.registerEntry(feedTitle, title, description, author, link, new SimpleDateFormat(DATE_FORMAT).parse(publishedDate));
    }
    
    public int getGroupCount() {
        return getGroups().size();
    }
    
    public List<SubscriptionGroupDto> getGroups() {
        return groupController.listAll(principal);
    }
    
    public Long createGroup(String title) {
        List<SubscriptionGroupDto> groups = groupController.create(principal, title);
        return Long.valueOf(groups.get(groups.size() - 1).getId());
    }
    
    public void deleteGroup(Long id) {
        groupController.delete(principal, id);
    }
    
    public void entitleGroup(Long id, String newTitle) {
        groupController.entitle(principal, id, newTitle);
    }
    
    public void moveGroup(Long id, String direction) {
        groupController.move(principal, id, "up".equals(direction));
    }
    
    public int getSubscriptionCount(Long groupId) throws Exception {
        return getSubscriptions(groupId).size();
    }
    
    public List<SubscriptionDto> getSubscriptions(Long groupId) {
        for (SubscriptionGroupDto group : groupController.listAll(principal)) {
            if (groupId.equals(Long.valueOf(group.getId()))) {
                return group.getSubscriptions();
            }
        }
        return null;
    }
    
    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (SubscriptionGroupDto group : groupController.listAll(principal)) {
            for (SubscriptionDto dto : group.getSubscriptions()) {
                Subscription subscription = new Subscription();
                subscription.setId(Long.valueOf(dto.getId()));
                subscription.setTitle(dto.getTitle());
                subscription.setGroupId(Long.valueOf(group.getId()));
                subscription.setGroupTitle(group.getTitle());
                subscriptions.add(subscription);
            }
        }
        return subscriptions;
    }
    
    public Long subscribe(String feed, Long groupId) throws Exception {
        subscriptionController.create(principal, groupId, feedRegistry.getUrl(feed));
        List<SubscriptionDto> subscriptions = getSubscriptions(groupId);
        return Long.valueOf(subscriptions.get(subscriptions.size() - 1).getId());
    }
    
    public void deleteSubscription(Long groupId, Long subscriptionId) {
        subscriptionController.delete(principal, groupId, subscriptionId);
    }
    
    public void entitleSubscription(Long groupId, Long subscriptionId, String title) {
        subscriptionController.entitle(principal, groupId, subscriptionId, title);
    }
    
    public void moveSubscription(Long groupId, Long subscriptionId, String direction) {
        subscriptionController.move(principal, groupId, subscriptionId, "up".equals(direction));
    }
    
    public String getEntryId(String title) {
        List<FeedEntryDto> entries = entryController.list(principal, "all", 0, Integer.MAX_VALUE, true);
        for (FeedEntryDto entry : entries) {
            if (title.equals(entry.getTitle())) {
                return entry.getId();
            }
        }
        return null;
    }
    
    public List<Entry> getEntries(String selection, int from, int to, String order) {
        return convertEntries(entryController.list(principal, selection, from, to - from, "ascending".equals(order)));
    }
    
    public List<Entry> getEntries(Long groupId, String selection, int from, int to, String order) {
        return convertEntries(entryController.list(principal, groupId, selection, from, to - from, "ascending".equals(order)));
    }
    
    public List<Entry> getEntries(Long groupId, Long subscriptionId, String selection, int from, int to, String order) {
        return convertEntries(entryController.list(principal, groupId, subscriptionId, selection, from, to - from, "ascending".equals(order)));
    }
    
    private static List<Entry> convertEntries(List<FeedEntryDto> dtos) {
        List<Entry> entries = new ArrayList<Entry>();
        for (FeedEntryDto dto : dtos) {
            Entry entry = new Entry();
            entry.setId(dto.getId());
            entry.setTitle(dto.getTitle());
            entry.setDescription(dto.getDescription());
            entry.setAuthor(dto.getAuthor());
            entry.setLink(dto.getLink());
            entry.setPublishedDate(dto.getPublishedDate());
            entry.setSubscriptionTitle(dto.getSubscriptionTitle());
            entry.setSubscriptionGroupId(dto.getSubscriptionGroupId());
            entry.setSubscriptionId(dto.getSubscriptionId());
            entry.setRead(dto.isRead());
            entry.setStarred(dto.isStarred());
            entries.add(entry);
        }
        return entries;
    }
    
    public void read(Long groupId, Long subscriptionId, Long entryId) {
        List<Long> idList = new ArrayList<Long>();
        idList.add(entryId);
        Map<Long, List<Long>> subscriptionMap = new HashMap<Long, List<Long>>();
        subscriptionMap.put(subscriptionId, idList);
        Map<Long, Map<Long, List<Long>>> ids = new HashMap<Long, Map<Long, List<Long>>>();
        ids.put(groupId, subscriptionMap);
        entryController.readAll(principal, ids);
    }
    
    public void markStarred(Long groupId, Long subscriptionId, Long entryId, String starred) {
        entryController.setStarred(principal, groupId, subscriptionId, entryId, "starred".equals(starred));
    }
    
    public int getArchiveCount() {
        return getArchives().size();
    }
    
    public List<ArchiveDto> getArchives() {
        return archiveController.listAll(principal);
    }
    
    public Long createArchive(String title) {
        List<ArchiveDto> archives = archiveController.create(principal, title);
        return Long.valueOf(archives.get(archives.size() - 1).getId());
    }
    
    public void deleteArchive(Long id) {
        archiveController.delete(principal, id);
    }
    
    public void entitleArchive(Long id, String newTitle) {
        archiveController.entitle(principal, id, newTitle);
    }
    
    public void moveArchive(Long id, String direction) {
        archiveController.move(principal, id, "up".equals(direction));
    }
    
    public List<ArchivedEntry> getArchivedEntries(int from, int to, String order) {
        return convertArchivedEntries(archivedEntryController.list(principal, from, to - from, "ascending".equals(order)));
    }
    
    public List<ArchivedEntry> getArchivedEntries(Long archiveId, int from, int to, String order) {
        return convertArchivedEntries(archivedEntryController.list(principal, archiveId, from, to - from, "ascending".equals(order)));
    }
    
    private static List<ArchivedEntry> convertArchivedEntries(List<ArchivedEntryDto> dtos) {
        List<ArchivedEntry> entries = new ArrayList<ArchivedEntry>();
        for (ArchivedEntryDto dto : dtos) {
            ArchivedEntry entry = new ArchivedEntry();
            entry.setId(dto.getId());
            entry.setTitle(dto.getTitle());
            entry.setDescription(dto.getDescription());
            entry.setAuthor(dto.getAuthor());
            entry.setLink(dto.getLink());
            entry.setPublishedDate(dto.getPublishedDate());
            entry.setArchiveId(dto.getArchiveId());
            entry.setArchiveTitle(dto.getArchiveTitle());
            entries.add(entry);
        }
        return entries;
    }
    
    public void archiveEntry(Long groupId, Long subscriptionId, Long entryId, Long archiveId) {
        archivedEntryController.archive(principal, archiveId, groupId, subscriptionId, entryId);
    }
    
    public void deleteArchivedEntry(Long archiveId, Long entryId) {
        archivedEntryController.delete(principal, archiveId, entryId);
    }
    
    public String getArchivedEntryId(String title) {
        List<ArchivedEntryDto> entries = archivedEntryController.list(principal, 0, Integer.MAX_VALUE, true);
        for (ArchivedEntryDto entry : entries) {
            if (title.equals(entry.getTitle())) {
                return entry.getId();
            }
        }
        return null;
    }
    
    public void refreshFeed(String title) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-AppEngine-TaskName")).thenReturn("default");
        taskController.refreshFeed(request, principal, feedRegistry.getUrl(title));
    }
    
    public void cleanupFeed(String title) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-AppEngine-TaskName")).thenReturn("default");
        taskController.cleanup(request, principal, feedRegistry.getUrl(title));
    }
    
    public void setCurrentDate(String dateString) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        when(dateHelper.addDaysToCurrentDate(-30)).thenReturn(date.getTime() - 30 * 24 * 60 * 60 * 1000L);
    }

}
