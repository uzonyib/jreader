package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.PostFilter;
import jreader.dao.PostFilter.PostType;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;

public class PostDaoImplTest extends AbstractDaoTest {

    private static final String SORT_PROPERTY = "publishDate";

    private UserDao userDao;
    private FeedDao feedDao;
    private GroupDao groupDao;
    private SubscriptionDao subscriptionDao;

    private PostDao sut;

    private User user;
    private List<Feed> feeds;
    private List<Group> groups;
    private List<Subscription> subscriptions;
    private List<Post> posts;

    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        groupDao = new GroupDaoImpl();
        subscriptionDao = new SubscriptionDaoImpl();
        sut = new PostDaoImpl();

        user = userDao.save(new User("test_user", Role.USER, 10L));

        List<Feed> feedsToBeSaved = Arrays.asList(
                Feed.builder().url("url_1").title("title_1").description("desc_1").feedType("feed_type_1").lastUpdateDate(100L).lastRefreshDate(200L)
                        .status(0).build(),
                        Feed.builder().url("url_2").title("title_2").description("desc_2").feedType("feed_type_2").lastUpdateDate(300L).lastRefreshDate(400L)
                        .status(1).build());
        feeds = new ArrayList<Feed>();
        for (Feed feed : feedsToBeSaved) {
            feeds.add(feedDao.save(feed));
        }

        List<Group> groupsToBeSaved = Arrays.asList(
                new Group(user, "group_1", 10),
                new Group(user, "group_2", 5));
        groups = new ArrayList<Group>();
        for (Group group : groupsToBeSaved) {
            groups.add(groupDao.save(group));
        }

        subscriptions = new ArrayList<Subscription>();
        Subscription subscription1 = new Subscription();
        subscription1.setTitle("title_1");
        subscription1.setOrder(11);
        subscription1.setLastUpdateDate(10L);
        subscription1.setFeed(feeds.get(0));
        subscription1.setGroup(groups.get(0));
        subscriptions.add(subscriptionDao.save(subscription1));
        Subscription subscription2 = new Subscription();
        subscription2.setTitle("title_2");
        subscription2.setOrder(6);
        subscription2.setLastUpdateDate(11L);
        subscription2.setFeed(feeds.get(1));
        subscription2.setGroup(groups.get(0));
        subscriptions.add(subscriptionDao.save(subscription2));

        long[] publishDates = { 103L, 102L, 101L, 104L };
        boolean[] readFlags = { true, true, false, false };
        boolean[] bookmarkedFlags = { false, true, true, false };

        posts = new ArrayList<Post>();
        for (int i = 0; i < publishDates.length; ++ i) {
            Post post = new Post();
            post.setUri("uri_" + i);
            post.setLink("link_" + i);
            post.setTitle("title_" + i);
            post.setDescription("description_" + i);
            post.setAuthor("author_" + i);
            post.setPublishDate(publishDates[i]);
            post.setRead(readFlags[i]);
            post.setBookmarked(bookmarkedFlags[i]);
            post.setSubscription(subscriptions.get(0));
            posts.add(sut.save(post));
        }
    }

    @Test
    public void findById_IfPostNotExists_ShouldReturnNull() {
        Post actual = sut.find(subscriptions.get(1), 1L);

        assertNull(actual);
    }

    @Test
    public void save_IfSubscriptionIsNew_ShouldReturnSubscription() {
        Post post = new Post();
        post.setUri("new_uri");
        post.setLink("new_link");
        post.setTitle("new_title");
        post.setDescription("new_description");
        post.setAuthor("new_author");
        post.setPublishDate(21L);
        post.setRead(false);
        post.setBookmarked(false);
        post.setSubscription(subscriptions.get(1));

        Post actual = sut.save(post);

        assertEquals(actual, post);
    }

    @Test
    public void findByUri_IfPostNotExists_ShouldReturnNull() {
        Post actual = sut.find(subscriptions.get(1), "not_found", 1L);

        assertNull(actual);
    }

    @Test
    public void findByUri_IfPostExists_ShouldReturnPost() {
        Post actual = sut.find(subscriptions.get(0), posts.get(0).getUri(), posts.get(0).getPublishDate());

        assertEquals(actual, posts.get(0));
    }

    @Test
    public void findById_IfPostExists_ShouldReturnPost() {
        Post actual = sut.find(subscriptions.get(0), posts.get(0).getId());

        assertEquals(actual, posts.get(0));
    }

    @Test
    public void findByOrdinal_IfPostExists_ShouldReturnPost() {
        Post actual = sut.find(subscriptions.get(0), 2);

        assertEquals(actual, posts.get(0));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void listForUser_IfSortIsNull_ShouldThrowException() {
        final PageRequest page = new PageRequest(0, 5, null);

        sut.list(user, new PostFilter(PostType.ALL, page));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void listForUser_IfSortPropertyInvalid_ShouldThrowException() {
        final PageRequest page = new PageRequest(0, 5, new Sort(Direction.ASC, "title"));

        sut.list(user, new PostFilter(PostType.ALL, page));
    }

    @Test
    public void listForUser_IfPostsExist_ShouldReturnPosts() {
        final PageRequest page = new PageRequest(1, 2, new Sort(Direction.ASC, SORT_PROPERTY));

        List<Post> actual = sut.list(user, new PostFilter(PostType.ALL, page));

        assertEquals(actual, Arrays.asList(posts.get(0), posts.get(3)));
    }

    @Test
    public void listForGroup_IfPostsExist_ShouldReturnPosts() {
        final PageRequest page = new PageRequest(0, 5, new Sort(Direction.DESC, SORT_PROPERTY));

        List<Post> actual = sut.list(groups.get(0), new PostFilter(PostType.UNREAD, page));

        assertEquals(actual, Arrays.asList(posts.get(3), posts.get(2)));
    }

    @Test
    public void listForSubscription_IfPostsExist_ShouldReturnPosts() {
        final PageRequest page = new PageRequest(0, 5, new Sort(Direction.ASC, SORT_PROPERTY));

        List<Post> actual = sut.list(subscriptions.get(0), new PostFilter(PostType.BOOKMARKED, page));

        assertEquals(actual, Arrays.asList(posts.get(2), posts.get(1)));
    }

    @Test
    public void listNotBookmarkedAndOlderThan_IfPostsExist_ShouldReturnPosts() {
        List<Post> actual = sut.listNotBookmarkedAndOlderThan(subscriptions.get(0), 104);

        assertEquals(actual, Arrays.asList(posts.get(0)));
    }

    @Test
    public void list_IfPostsExist_ShouldReturnPosts() {
        List<Post> actual = sut.list(subscriptions.get(0));

        assertEquals(actual, Arrays.asList(posts.get(0), posts.get(1), posts.get(2), posts.get(3)));
    }

    @Test
    public void countUnread_IfPostsExist_ShouldReturnCount() {
        int actual = sut.countUnread(subscriptions.get(0));

        assertEquals(actual, 2);
    }

}
