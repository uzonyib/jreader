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
    @Index
    private String title;
    @Index
    private int order;
    private Long updatedDate;
    
    public Subscription() {
        
    }

    private Subscription(final Builder builder) {
        this.setId(builder.id);
        this.setGroup(builder.group);
        this.setFeed(builder.feed);
        this.setTitle(builder.title);
        this.setOrder(builder.order);
        this.setUpdatedDate(builder.updatedDate);
    }

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

    public static class Builder {

        private Long id;
        private SubscriptionGroup group;
        private Feed feed;
        private String title;
        private int order;
        private Long updatedDate;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder group(final SubscriptionGroup group) {
            this.group = group;
            return this;
        }

        public Builder feed(final Feed feed) {
            this.feed = feed;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder order(final int order) {
            this.order = order;
            return this;
        }

        public Builder updatedDate(final Long updatedDate) {
            this.updatedDate = updatedDate;
            return this;
        }
        
        public Subscription build() {
            return new Subscription(this);
        }

    }

}
