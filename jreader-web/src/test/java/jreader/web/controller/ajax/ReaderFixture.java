package jreader.web.controller.ajax;

import static org.mockito.Mockito.doReturn;
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
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dao.PostFilter.PostType;
import jreader.domain.Role;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;
import jreader.dto.PostDto;
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.dto.SubscriptionDto;
import jreader.dto.GroupDto;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.RssService;
import jreader.services.ServiceException;
import jreader.services.StatService;
import jreader.services.UserService;
import jreader.services.impl.DateHelperImpl;
import jreader.web.controller.ajax.dto.ArchivedPost;
import jreader.web.controller.ajax.dto.Post;
import jreader.web.controller.ajax.dto.FeedStat;
import jreader.web.controller.ajax.dto.Subscription;
import jreader.web.controller.appengine.TaskController;
import jreader.web.service.QueueService;
import jreader.web.test.AbstractDataStoreTest;
import jreader.web.test.FeedRegistry;

@SuppressWarnings("unchecked")
public abstract class ReaderFixture extends AbstractDataStoreTest {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
    
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
        when(feedFetcher.retrieveFeed(new URL(url))).thenThrow(Exception.class);
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
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal).getPayload()) {
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
        return (List<GroupDto>) groupController.listAll(principal).getPayload();
    }
    
    public Long createGroup(String title) {
        try {
            List<GroupDto> groups = (List<GroupDto>) groupController.create(principal, "empty string".equals(title) ? "" : title)
                    .getPayload();
            return Long.valueOf(groups.get(groups.size() - 1).getId());
        } catch (ServiceException e) {
            return null;
        }
    }
    
    public void deleteGroup(Long id) {
        groupController.delete(principal, id);
    }
    
    public void entitleGroup(Long id, String title) {
        try {
            groupController.entitle(principal, id, "empty string".equals(title) ? "" : title);
        } catch (ServiceException e) {
            
        }
    }
    
    public void moveGroup(Long id, String direction) {
        try {
            groupController.move(principal, id, "up".equals(direction));
        } catch (ServiceException e) {
            
        }
    }
    
    public int getSubscriptionCount(Long groupId) throws Exception {
        return getSubscriptions(groupId).size();
    }
    
    public List<SubscriptionDto> getSubscriptions(Long groupId) {
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal).getPayload()) {
            if (groupId.equals(Long.valueOf(group.getId()))) {
                return group.getSubscriptions();
            }
        }
        return null;
    }
    
    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<Subscription>();
        for (GroupDto group : (List<GroupDto>) groupController.listAll(principal).getPayload()) {
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
        } catch (ServiceException e) {
            
        }
    }
    
    public void moveSubscription(Long groupId, Long subscriptionId, String direction) {
        try {
            subscriptionController.move(principal, groupId, subscriptionId, "up".equals(direction));
        } catch (ServiceException e) {
            
        }
    }
    
    public String getPostId(String title) {
        List<PostDto> posts = (List<PostDto>) postController.list(principal, PostType.ALL, 0, Integer.MAX_VALUE, true).getPayload();
        for (PostDto post : posts) {
            if (title.equals(post.getTitle())) {
                return post.getId();
            }
        }
        return null;
    }
    
    public List<Post> getPosts(String selection, int from, int to, String order) {
        return convertPosts((List<PostDto>) postController
                .list(principal, PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), from, to - from, "ascending".equals(order)).getPayload());
    }
    
    public List<Post> getPosts(Long groupId, String selection, int from, int to, String order) {
        return convertPosts((List<PostDto>) postController
                .list(principal, groupId, PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), from, to - from, "ascending".equals(order)).getPayload());
    }
    
    public List<Post> getPosts(Long groupId, Long subscriptionId, String selection, int from, int to, String order) {
        return convertPosts((List<PostDto>) postController.list(principal, groupId, subscriptionId,
                PostType.valueOf(selection.toUpperCase(Locale.ENGLISH)), from, to - from, "ascending".equals(order)).getPayload());
    }
    
    private static List<Post> convertPosts(List<PostDto> dtos) {
        List<Post> posts = new ArrayList<Post>();
        for (PostDto dto : dtos) {
            Post post = new Post();
            post.setId(dto.getId());
            post.setTitle(dto.getTitle());
            post.setDescription(dto.getDescription());
            post.setAuthor(dto.getAuthor());
            post.setLink(dto.getLink());
            post.setPublishDate(dto.getPublishDate());
            post.setSubscriptionTitle(dto.getSubscriptionTitle());
            post.setGroupId(dto.getGroupId());
            post.setSubscriptionId(dto.getSubscriptionId());
            post.setRead(dto.isRead());
            post.setStarred(dto.isStarred());
            posts.add(post);
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
    
    public void markStarred(Long groupId, Long subscriptionId, Long postId, String starred) {
        postController.setStarred(principal, groupId, subscriptionId, postId, "starred".equals(starred));
    }
    
    public int getArchiveCount() {
        return getArchives().size();
    }
    
    public List<ArchiveDto> getArchives() {
        return (List<ArchiveDto>) archiveController.listAll(principal).getPayload();
    }
    
    public Long createArchive(String title) {
        try {
            List<ArchiveDto> archives = (List<ArchiveDto>) archiveController.create(principal, "empty string".equals(title) ? "" : title).getPayload();
            return Long.valueOf(archives.get(archives.size() - 1).getId());
        } catch (ServiceException e) {
            return null;
        }
    }
    
    public void deleteArchive(Long id) {
        archiveController.delete(principal, id);
    }
    
    public void entitleArchive(Long id, String title) {
        try {
            archiveController.entitle(principal, id, "empty string".equals(title) ? "" : title);
        } catch (ServiceException e) {
            
        }
    }
    
    public void moveArchive(Long id, String direction) {
        try {
            archiveController.move(principal, id, "up".equals(direction));
        } catch (ServiceException e) {
            
        }
    }
    
    public List<ArchivedPost> getArchivedPosts(int from, int to, String order) {
        return convertArchivedPosts(
                (List<ArchivedPostDto>) archivedPostController.list(principal, from, to - from, "ascending".equals(order)).getPayload());
    }
    
    public List<ArchivedPost> getArchivedPosts(Long archiveId, int from, int to, String order) {
        return convertArchivedPosts(
                (List<ArchivedPostDto>) archivedPostController.list(principal, archiveId, from, to - from, "ascending".equals(order)).getPayload());
    }
    
    private static List<ArchivedPost> convertArchivedPosts(List<ArchivedPostDto> dtos) {
        List<ArchivedPost> posts = new ArrayList<ArchivedPost>();
        for (ArchivedPostDto dto : dtos) {
            ArchivedPost post = new ArchivedPost();
            post.setId(dto.getId());
            post.setTitle(dto.getTitle());
            post.setDescription(dto.getDescription());
            post.setAuthor(dto.getAuthor());
            post.setLink(dto.getLink());
            post.setPublishDate(dto.getPublishDate());
            post.setArchiveId(dto.getArchiveId());
            post.setArchiveTitle(dto.getArchiveTitle());
            posts.add(post);
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
        List<ArchivedPostDto> posts = (List<ArchivedPostDto>) archivedPostController.list(principal, 0, Integer.MAX_VALUE, true).getPayload();
        for (ArchivedPostDto post : posts) {
            if (title.equals(post.getTitle())) {
                return post.getId();
            }
        }
        return null;
    }
    
    public void refreshFeed(String title) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-AppEngine-TaskName")).thenReturn("default");
        taskController.refreshFeed(request, feedRegistry.getUrl(title));
    }
    
    public void cleanupFeed(String title) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-AppEngine-TaskName")).thenReturn("default");
        taskController.cleanup(request, feedRegistry.getUrl(title));
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
        List<FeedStatsDto> stats = (List<FeedStatsDto>) statController.list(principal).getPayload();
        for (FeedStatsDto stat : stats) {
            if (stat.getFeed().getTitle().equals(feed)) {
                return convertFeedStats(stat.getStats());
            }
        }
        return null;
    }
    
    private static List<FeedStat> convertFeedStats(List<FeedStatDto> dtos) {
        List<FeedStat> stats = new ArrayList<FeedStat>();
        for (FeedStatDto dto : dtos) {
            stats.add(new FeedStat(dto));
        }
        return stats;
    }

}
