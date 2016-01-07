package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.dao.PostDao;
import jreader.dao.PostFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.dao.PostFilter.PostType;
import jreader.domain.Feed;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostDaoImplTest extends AbstractDaoTest {
    
    private static final String USERNAME = "test_user";
    
    private static final String[] GROUP_TITLES = { "group_1", "group_2" };
    private static final int[] GROUP_ORDERS = { 10, 5 };
    private static List<Group> savedGroups;
    
    private static final String[] FEED_URLS = { "url_1", "url_2" };
    private static List<Feed> savedFeeds;
    
    private static final String[] SUBSCRIPTION_TITLES = { "title_1", "title_2" };
    private static final int[] SUBSCRIPTION_ORDERS = { 11, 6 };
    private static final long[] SUBSCRIPTION_UPDATED_DATES = { 10L, 11L };
    private static List<Subscription> savedSubscriptions;
    
    private static final String[] POST_URIS = { "uri_1", "uri_2", "uri_3", "uri_4" };
    private static final String[] POST_LINKS = { "link_1", "link_2", "link_3", "link_4" };
    private static final String[] POST_TITLES = { "title_1", "title_2", "title_3", "title_4" };
    private static final String[] POST_DESCRIPTIONS = { "description_1", "description_2", "description_3", "description_4" };
    private static final String[] POST_AUTHORS = { "author_1", "author_2", "author_3", "author_4" };
    private static final long[] POST_PUBLISHED_DATES = { 103L, 102L, 101L, 104L };
    private static final boolean[] POST_READ_FLAGS = { true, true, false, false };
    private static final boolean[] POST_BOOKMARKED_FLAGS = { false, true, true, false };
    private static List<Post> savedPosts;
    
    private static final String NEW_URI = "new_uri";
    private static final String NEW_LINK = "new_link";
    private static final String NEW_TITLE = "new_title";
    private static final String NEW_DESCRIPTION = "new_description";
    private static final String NEW_AUTHOR = "new_author";
    private static final long NEW_PUBLISHED_DATE = 105L;
    private static final boolean NEW_READ_FLAG = false;
    private static final boolean NEW_BOOKMARKED_FLAG = false;
    
    private UserDao userDao;
    private FeedDao feedDao;
    private GroupDao groupDao;
    private SubscriptionDao subscriptionDao;

    private PostDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        groupDao = new GroupDaoImpl();
        subscriptionDao = new SubscriptionDaoImpl();
        sut = new PostDaoImpl();
        
        User user = new User();
        user.setUsername(USERNAME);
        userDao.save(user);
        
        savedFeeds = new ArrayList<Feed>();
        for (int i = 0; i < FEED_URLS.length; ++i) {
            Feed feed = new Feed();
            feed.setUrl(FEED_URLS[i]);
            feed.setTitle("title_" + i);
            feed.setDescription("description_" + i);
            feed.setFeedType("feedType_" + i);
            feed.setLastUpdateDate(1000L);
            savedFeeds.add(feedDao.save(feed));
        }
        
        savedGroups = new ArrayList<Group>();
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            Group group = new Group();
            group.setUser(user);
            group.setTitle(GROUP_TITLES[i]);
            group.setOrder(GROUP_ORDERS[i]);
            savedGroups.add(groupDao.save(group));
        }
        
        savedSubscriptions = new ArrayList<Subscription>();
        for (int i = 0; i < SUBSCRIPTION_TITLES.length; ++ i) {
            Subscription subscription = new Subscription();
            subscription.setTitle(SUBSCRIPTION_TITLES[i]);
            subscription.setOrder(SUBSCRIPTION_ORDERS[i]);
            subscription.setLastUpdateDate(SUBSCRIPTION_UPDATED_DATES[i]);
            subscription.setFeed(savedFeeds.get(i));
            subscription.setGroup(savedGroups.get(0));
            savedSubscriptions.add(subscriptionDao.save(subscription));
        }
        
        savedPosts = new ArrayList<Post>();
        for (int i = 0; i < POST_TITLES.length; ++ i) {
            Post post = new Post();
            post.setUri(POST_URIS[i]);
            post.setLink(POST_LINKS[i]);
            post.setTitle(POST_TITLES[i]);
            post.setDescription(POST_DESCRIPTIONS[i]);
            post.setAuthor(POST_AUTHORS[i]);
            post.setLink(POST_LINKS[i]);
            post.setPublishDate(POST_PUBLISHED_DATES[i]);
            post.setRead(POST_READ_FLAGS[i]);
            post.setBookmarked(POST_BOOKMARKED_FLAGS[i]);
            post.setSubscription(savedSubscriptions.get(0));
            savedPosts.add(sut.save(post));
        }
    }

    @Test
    public void findById_IfPostNotExists_ShouldReturnNull() {
        Post post = sut.find(savedSubscriptions.get(1), 1L);
        
        assertNull(post);
    }
    
    @Test
    public void save_IfSubscriptionIsNew_ShouldReturnSubscription() {
        Post post = new Post();
        post.setUri(NEW_URI);
        post.setLink(NEW_LINK);
        post.setTitle(NEW_TITLE);
        post.setDescription(NEW_DESCRIPTION);
        post.setAuthor(NEW_AUTHOR);
        post.setPublishDate(NEW_PUBLISHED_DATE);
        post.setRead(NEW_READ_FLAG);
        post.setBookmarked(NEW_BOOKMARKED_FLAG);
        post.setSubscription(savedSubscriptions.get(1));

        post = sut.save(post);
        
        assertNotNull(post);
        assertNotNull(post.getId());
        assertEquals(post.getSubscription().getTitle(), SUBSCRIPTION_TITLES[1]);
        assertEquals(post.getUri(), NEW_URI);
        assertEquals(post.getLink(), NEW_LINK);
        assertEquals(post.getTitle(), NEW_TITLE);
        assertEquals(post.getDescription(), NEW_DESCRIPTION);
        assertEquals(post.getAuthor(), NEW_AUTHOR);
        assertEquals(post.getPublishDate().longValue(), NEW_PUBLISHED_DATE);
        assertEquals(post.isRead(), NEW_READ_FLAG);
        assertEquals(post.isBookMarked(), NEW_BOOKMARKED_FLAG);
    }
    
    @Test
    public void findByUri_IfPostNotExists_ShouldReturnNull() {
        Post post = sut.find(savedSubscriptions.get(1), "not_found", 1L);
        
        assertNull(post);
    }
    
    @Test
    public void findByUri_IfPostExists_ShouldReturnPost() {
        Post post = sut.find(savedSubscriptions.get(0), savedPosts.get(0).getUri(), savedPosts.get(0).getPublishDate());
        
        assertNotNull(post);
        assertNotNull(post.getId());
        assertEquals(post.getSubscription().getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(post.getUri(), POST_URIS[0]);
        assertEquals(post.getLink(), POST_LINKS[0]);
        assertEquals(post.getTitle(), POST_TITLES[0]);
        assertEquals(post.getDescription(), POST_DESCRIPTIONS[0]);
        assertEquals(post.getAuthor(), POST_AUTHORS[0]);
        assertEquals(post.getPublishDate().longValue(), POST_PUBLISHED_DATES[0]);
        assertEquals(post.isRead(), POST_READ_FLAGS[0]);
        assertEquals(post.isBookMarked(), POST_BOOKMARKED_FLAGS[0]);
    }
    
    @Test
    public void findById_IfPostExists_ShouldReturnPost() {
        Post post = sut.find(savedSubscriptions.get(0), savedPosts.get(0).getId());
        
        assertNotNull(post);
        assertNotNull(post.getId());
        assertEquals(post.getSubscription().getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(post.getUri(), POST_URIS[0]);
        assertEquals(post.getLink(), POST_LINKS[0]);
        assertEquals(post.getTitle(), POST_TITLES[0]);
        assertEquals(post.getDescription(), POST_DESCRIPTIONS[0]);
        assertEquals(post.getAuthor(), POST_AUTHORS[0]);
        assertEquals(post.getPublishDate().longValue(), POST_PUBLISHED_DATES[0]);
        assertEquals(post.isRead(), POST_READ_FLAGS[0]);
        assertEquals(post.isBookMarked(), POST_BOOKMARKED_FLAGS[0]);
    }
    
    @Test
    public void findByOrdinal_IfPostExists_ShouldReturnPost() {
        Post post = sut.find(savedSubscriptions.get(0), 2);
        
        assertNotNull(post);
        assertNotNull(post.getId());
        assertEquals(post.getId(), savedPosts.get(0).getId());
    }
    
    @Test
    public void listForUser_IfPostsExist_ShouldReturnPosts() {
        User user = new User();
        user.setUsername(USERNAME);
        PostFilter filter = new PostFilter(PostType.ALL, true, 1, 2);
        
        List<Post> posts = sut.list(user, filter);
        
        assertNotNull(posts);
        assertEquals(posts.size(), 2);
        assertEquals(posts.get(0).getId(), savedPosts.get(1).getId());
        assertEquals(posts.get(1).getId(), savedPosts.get(0).getId());
        
    }
    
    @Test
    public void listForGroup_IfPostsExist_ShouldReturnPosts() {
        PostFilter filter = new PostFilter(PostType.UNREAD, false, 0, 5);
        
        List<Post> posts = sut.list(savedGroups.get(0), filter);
        
        assertNotNull(posts);
        assertEquals(posts.size(), 2);
        assertEquals(posts.get(0).getId(), savedPosts.get(3).getId());
        assertEquals(posts.get(1).getId(), savedPosts.get(2).getId());
        
    }
    
    @Test
    public void listForSubscription_IfPostsExist_ShouldReturnPosts() {
        PostFilter filter = new PostFilter(PostType.BOOKMARKED, true, 0, 5);
        
        List<Post> posts = sut.list(savedSubscriptions.get(0), filter);
        
        assertNotNull(posts);
        assertEquals(posts.size(), 2);
        assertEquals(posts.get(0).getId(), savedPosts.get(2).getId());
        assertEquals(posts.get(1).getId(), savedPosts.get(1).getId());
        
    }
    
    @Test
    public void listNotBookmarkedAndOlderThan_IfPostsExist_ShouldReturnPosts() {
        List<Post> posts = sut.listNotBookmarkedAndOlderThan(savedSubscriptions.get(0), 104);
        
        assertNotNull(posts);
        assertEquals(posts.size(), 1);
        assertEquals(posts.get(0).getId(), savedPosts.get(0).getId());
        
    }
    
    @Test
    public void list_IfPostsExist_ShouldReturnPosts() {
        List<Post> posts = sut.list(savedSubscriptions.get(0));
        
        assertNotNull(posts);
        assertEquals(posts.size(), 4);
    }
    
    @Test
    public void countUnread_IfPostsExist_ShouldReturnCount() {
        int count = sut.countUnread(savedSubscriptions.get(0));
        
        assertEquals(count, 2);
    }

}
