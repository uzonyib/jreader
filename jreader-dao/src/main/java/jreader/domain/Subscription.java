package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class Subscription {

    @Id
    private Long id;
    @Load
    @Parent
    private Ref<SubscriptionGroup> groupRef;
    @Load
    @Index
    private Ref<Feed> feedRef;
    private String title;
    @Index
    private int order;
    private Long updatedDate;
    private Long refreshDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Ref<SubscriptionGroup> getGroupRef() {
        return groupRef;
    }

    public void setGroupRef(final Ref<SubscriptionGroup> groupRef) {
        this.groupRef = groupRef;
    }

    public Ref<Feed> getFeedRef() {
        return feedRef;
    }

    public void setFeedRef(final Ref<Feed> feedRef) {
        this.feedRef = feedRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public SubscriptionGroup getGroup() {
        return getGroupRef() == null ? null : getGroupRef().get();
    }

    public void setGroup(final SubscriptionGroup group) {
        this.setGroupRef(group == null ? null : Ref.create(group));
    }

    public Feed getFeed() {
        return getFeedRef() == null ? null : getFeedRef().get();
    }

    public void setFeed(final Feed feed) {
        this.setFeedRef(feed == null ? null : Ref.create(feed));
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(final Long refreshDate) {
        this.refreshDate = refreshDate;
    }

}
