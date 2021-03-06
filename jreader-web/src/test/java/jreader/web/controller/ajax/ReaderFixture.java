package jreader.web.controller.ajax;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dao.PostFilter.PostType;
import jreader.domain.Role;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.dto.GroupDto;
import jreader.dto.PostDto;
import jreader.dto.SubscriptionDto;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.FeedFetcher;
import jreader.services.RssService;
import jreader.services.StatService;
import jreader.services.UserService;
import jreader.services.exception.FetchException;
import jreader.services.impl.DateHelperImpl;
import jreader.web.controller.ajax.dto.ArchivedPost;
import jreader.web.controller.ajax.dto.FeedStat;
import jreader.web.controller.ajax.dto.Post;
import jreader.web.controller.ajax.dto.Subscription;
import jreader.web.controller.appengine.TaskController;
import jreader.web.service.QueueService;
import jreader.web.test.AbstractDataStoreTest;
import jreader.web.test.FeedRegistry;

public abstract class ReaderFixture extends AbstractDataStoreTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
    private static final String SORT_PROPERTY = "publishDate";

    @Autowired
    private GroupController groupController;
    @Autowired
    @InjectMocks
    private SubscriptionController subscriptionController;
    @Autowired
    private PostController postController;
    @Autowired
    private ArchiveController archiveController;
    @Autowired
    private ArchivedPostController archivedPostController;
    @Autowired
    @InjectMocks
    private TaskController taskController;
    @Autowired
    private StatController statController;

    @Autowired
    @InjectMocks
    private UserService userService;
    @Autowired
    @InjectMocks
    private RssService rssService;
    @Autowired
    @InjectMocks
    private CronService cronService;
    @Autowired
    @InjectMocks
    private StatService statService;

    @Mock
    private Principal principal;
    @Mock
    private com.google.appengine.api.users.UserService googleUserService;
    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private DateHelper dateHelper;
    @Mock
    private QueueService queueService;
    
    private FeedRegistry feedRegistry = new FeedRegistry();
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    public void initUser(String username) {
    	when(googleUserService.isUserAdmin()).thenReturn(true);
        userService.register(username, Role.ADMIN);
        when(principal.getName()).thenReturn(username);
    }
    
    public void createFeed(String title, String url) throws Exception {
        SyndFeed feed = feedRegistry.registerFeed(title, url);
        when(feedFetcher.retrieveFeed(new URL(url))).thenReturn(feed);
    }
    
    public void setFeedUnavailable(String title) throws Exception {
        String url = feedRegistry.getUrl(title);
        when(feedFetcher.retrieveFeed(new URL(url))).thenThrow(new FetchException());
    }
    
    public void setFeedAvailable(String title) throws Exception {
        String url = feedRegistry.getUrl(title);
        SyndFeed feed = feedRegistry.getFeed(title);
        doReturn(feed).when(feedFetcher).retrieveFeed(new URL(url));
    }
    
    public void createPost(String feedTitle, String uri, String title, String description, String author, String link, String publishDate)
            throws Exception {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(publishDate);
        feedRegistry.registerPost(feedTitle, uri, title, description, author, link, date);
        when(dateHelper.getFirstSecondOfDay(date.getTime())).thenReturn(new DateHelperImpl().getFirstSecondOfDay(date.getTime()));
    }
    
    public Integer getFeedStatus(String title) {
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal)) {
            for (SubscriptionDto dto : group.getSubscriptions()) {
                if (title.equals(dto.getFeed().getTitle())) {
                    return dto.getFeed().getStatus();
                }
            }
        }
        return null;
    }
    
    public int getGroupCount() {
        return getGroups().size();
    }
    
    public List<GroupDto> getGroups() {
        return (List<GroupDto>) groupController.listAll(principal);
    }
    
    public Long createGroup(String title) {
        try {
            GroupDto group = GroupDto.builder().title("empty string".equals(title) ? "" : title).build();
            return groupController.create(principal, group).getId();
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteGroup(Long id) {
        groupController.delete(principal, id);
    }
    
    public void entitleGroup(Long id, String title) {
        try {
            groupController.update(principal, id, GroupDto.builder().title("empty string".equals(title) ? "" : title).build());
        } catch (Exception e) {
            
        }
    }
    
    public void reorderGroups(String titles) {
        List<GroupDto> groups = (List<GroupDto>) groupController.listAll(principal);
        List<GroupDto> input = new ArrayList<>();
        for (String title : titles.split(", ")) {
            for (GroupDto group : groups) {
                if (group.getTitle().equals(title)) {
                    input.add(GroupDto.builder().id(group.getId()).build());
                    break;
                }
            }
        }
        groupController.update(principal, input);
    }

    public int getSubscriptionCount(Long groupId) {
        return getSubscriptions(groupId).size();
    }
    
    public List<SubscriptionDto> getSubscriptions(Long groupId) {
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal)) {
            if (groupId.equals(Long.valueOf(group.getId()))) {
                return group.getSubscriptions();
            }
        }
        return null;
    }
    
    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal)) {
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
    
    public Long subscribe(String feed, Long groupId) {
        subscriptionController.create(principal, groupId, feedRegistry.getUrl(feed));
        refreshFeed(feed);
        List<SubscriptionDto> subscriptions = getSubscriptions(groupId);
        return Long.valueOf(subscriptions.get(subscriptions.size() - 1).getId());
    }
    
    public void deleteSubscription(Long groupId, Long subscriptionId) {
        subscriptionController.delete(principal, groupId, subscriptionId);
    }
    
    public void entitleSubscription(Long groupId, Long subscriptionId, String title) {
        try {
            subscriptionController.entitle(principal, groupId, subscriptionId, "empty string".equals(title) ? "" : title);
        } catch (Exception e) {
            
        }
    }
    
    public void moveSubscription(Long groupId, Long subscriptionId, String direction) {
        try {
            subscriptionController.move(principal, groupId, subscriptionId, "up".equals(direction));
        } catch (Exception e) {
            
        }
    }
    
    public String getPostId(String title) {
        final PageRequest page = new PageRequest(0, Integer.MAX_VALUE, new Sort(Direction.ASC, SORT_PROPERTY));
        List<PostDto> posts = (List<PostDto>) postController.list(principal, PostType.ALL, page);
        for (PostDto post : posts) {
            if (title.equals(post.getTitle())) {
                return post.getId();
            }
        }
        return null;
    }
    
    public List<Post> getPosts(String selection, int from, int to, String order) {
        final PageRequest page = new PageRequest(getPageNumber(from, to), getPageSize(from, to),
                new Sort("ascending".equals(order) ? Direction.ASC : Direction.DESC, SORT_PROPERTY));
        return convertPosts((List<PostDto>) postController.list(principal, PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), page));
    }
    
    public List<Post> getPosts(Long groupId, String selection, int from, int to, String order) {
        final PageRequest page = new PageRequest(getPageNumber(from, to), getPageSize(from, to),
                new Sort("ascending".equals(order) ? Direction.ASC : Direction.DESC, SORT_PROPERTY));
        return convertPosts((List<PostDto>) postController.list(principal, groupId, PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), page));
    }
    
    public List<Post> getPosts(Long groupId, Long subscriptionId, String selection, int from, int to, String order) {
        final PageRequest page = new PageRequest(getPageNumber(from, to), getPageSize(from, to),
                new Sort("ascending".equals(order) ? Direction.ASC : Direction.DESC, SORT_PROPERTY));
        return convertPosts(
                (List<PostDto>) postController.list(principal, groupId, subscriptionId, PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), page));
    }
    
    private static List<Post> convertPosts(List<PostDto> dtos) {
        List<Post> posts = new ArrayList<Post>();
        for (PostDto dto : dtos) {
            posts.add(new Post(dto));
        }
        return posts;
    }
    
    public void read(Long groupId, Long subscriptionId, Long postId) {
        List<Long> idList = new ArrayList<Long>();
        idList.add(postId);
        Map<Long, List<Long>> subscriptionMap = new HashMap<Long, List<Long>>();
        subscriptionMap.put(subscriptionId, idList);
        Map<Long, Map<Long, List<Long>>> ids = new HashMap<Long, Map<Long, List<Long>>>();
        ids.put(groupId, subscriptionMap);
        postController.readAll(principal, ids);
    }
    
    public void updateBookmarked(Long groupId, Long subscriptionId, Long postId, String bookmarked) {
        postController.setBookmarked(principal, groupId, subscriptionId, postId, "bookmarked".equals(bookmarked));
    }
    
    public int getArchiveCount() {
        return getArchives().size();
    }
    
    public List<ArchiveDto> getArchives() {
        return (List<ArchiveDto>) archiveController.listAll(principal);
    }
    
    public Long createArchive(String title) {
        try {
            List<ArchiveDto> archives = (List<ArchiveDto>) archiveController.create(principal, "empty string".equals(title) ? "" : title);
            return Long.valueOf(archives.get(archives.size() - 1).getId());
        } catch (Exception e) {
            return null;
        }
    }
    
    public void deleteArchive(Long id) {
        archiveController.delete(principal, id);
    }
    
    public void entitleArchive(Long id, String title) {
        try {
            archiveController.entitle(principal, id, "empty string".equals(title) ? "" : title);
        } catch (Exception e) {
            
        }
    }
    
    public void moveArchive(Long id, String direction) {
        try {
            archiveController.move(principal, id, "up".equals(direction));
        } catch (Exception e) {
            
        }
    }
    
    public List<ArchivedPost> getArchivedPosts(int from, int to, String order) {
        final PageRequest page = new PageRequest(getPageNumber(from, to), getPageSize(from, to),
                new Sort("ascending".equals(order) ? Direction.ASC : Direction.DESC, SORT_PROPERTY));
        return convertArchivedPosts((List<ArchivedPostDto>) archivedPostController.list(principal, page));
    }

    public List<ArchivedPost> getArchivedPosts(Long archiveId, int from, int to, String order) {
        final PageRequest page = new PageRequest(getPageNumber(from, to), getPageSize(from, to),
                new Sort("ascending".equals(order) ? Direction.ASC : Direction.DESC, SORT_PROPERTY));
        return convertArchivedPosts((List<ArchivedPostDto>) archivedPostController.list(principal, archiveId, page));
    }
    
    private static List<ArchivedPost> convertArchivedPosts(List<ArchivedPostDto> dtos) {
        List<ArchivedPost> posts = new ArrayList<ArchivedPost>();
        for (ArchivedPostDto dto : dtos) {
            posts.add(new ArchivedPost(dto));
        }
        return posts;
    }
    
    public void archivePost(Long groupId, Long subscriptionId, Long postId, Long archiveId) {
        archivedPostController.archive(principal, archiveId, groupId, subscriptionId, postId);
    }
    
    public void deleteArchivedPost(Long archiveId, Long postId) {
        archivedPostController.delete(principal, archiveId, postId);
    }
    
    public String getArchivedPostId(String title) {
        final PageRequest page = new PageRequest(0, Integer.MAX_VALUE, new Sort(Direction.ASC, SORT_PROPERTY));
        List<ArchivedPostDto> posts = (List<ArchivedPostDto>) archivedPostController.list(principal, page);
        for (ArchivedPostDto post : posts) {
            if (title.equals(post.getTitle())) {
                return post.getId();
            }
        }
        return null;
    }
    
    public void refreshFeed(String title) {
        taskController.refreshFeed(feedRegistry.getUrl(title));
    }
    
    public void cleanupFeed(String title) {
        taskController.cleanup(feedRegistry.getUrl(title));
    }
    
    public void setCurrentDate(String dateString) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(dateString);
        when(dateHelper.getCurrentDate()).thenReturn(date.getTime());
        when(dateHelper.getFirstSecondOfDay(date.getTime())).thenReturn(new DateHelperImpl().getFirstSecondOfDay(date.getTime()));
        long millis = date.getTime() - 30 * 24 * 60 * 60 * 1000L;
        when(dateHelper.substractDaysFromCurrentDate(30)).thenReturn(millis);
        when(dateHelper.getFirstSecondOfDay(millis)).thenReturn(new DateHelperImpl().getFirstSecondOfDay(millis));
        millis = date.getTime() - 20 * 24 * 60 * 60 * 1000L;
        when(dateHelper.substractDaysFromCurrentDate(20)).thenReturn(millis);
    }
    
    public List<FeedStat> getStats(String feed) {
        List<FeedStatsDto> stats = (List<FeedStatsDto>) statController.list(principal);
        for (FeedStatsDto stat : stats) {
            if (stat.getFeed().getTitle().equals(feed)) {
                return convertFeedStats(stat.getStats());
            }
        }
        return null;
    }

    private static int getPageSize(int from, int to) {
        return to - from;
    }

    private static int getPageNumber(int from, int to) {
        return to / getPageSize(from, to) - 1;
    }
    
    private static List<FeedStat> convertFeedStats(List<FeedStatDto> dtos) {
        List<FeedStat> stats = new ArrayList<FeedStat>();
        for (FeedStatDto dto : dtos) {
            stats.add(new FeedStat(dto));
        }
        return stats;
    }

}
