package jreader.web.controller.ajax;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.RssService;
import jreader.services.UserService;
import jreader.web.controller.ajax.dto.Subscription;
import jreader.web.test.AbstractDataStoreTest;

public abstract class ReaderFixture extends AbstractDataStoreTest {

    @Autowired
    protected GroupController groupController;
    @Autowired
    protected SubscriptionController subscriptionController;
    
    @Autowired
    private UserService userService;
    @Autowired
    @InjectMocks
    private RssService rssService;

    @Mock
    protected Principal principal;
    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private SyndFeed syndFeed;
    private Map<String, String> feedUrls = new HashMap<String, String>();
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    public void initUser(String username) {
        userService.register(username);
        when(principal.getName()).thenReturn(username);
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
    
    public void initFeed(String title, String url) throws Exception {
        SyndFeed feed = mock(SyndFeed.class);
        when(feed.getTitle()).thenReturn(title);
        when(feedFetcher.retrieveFeed(new URL(url))).thenReturn(feed);
        feedUrls.put(title, url);
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
        subscriptionController.create(principal, groupId, feedUrls.get(feed));
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

}
