package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Cache
@Data
@NoArgsConstructor
public class Subscription {

    @Id
    private Long id;
    @Load
    @Parent
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<Group> groupRef;
    @Load
    @Index
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<Feed> feedRef;
    @Index
    private String title;
    @Index
    private int order;
    private Long lastUpdateDate;

    private Subscription(final SubscriptionBuilder builder) {
        this.setId(builder.id);
        this.setGroup(builder.group);
        this.setFeed(builder.feed);
        this.setTitle(builder.title);
        this.setOrder(builder.order);
        this.setLastUpdateDate(builder.lastUpdateDate);
    }
    
    public Group getGroup() {
        return getGroupRef() == null ? null : getGroupRef().get();
    }
    
    public void setGroup(final Group group) {
        this.setGroupRef(group == null ? null : Ref.create(group));
    }
    
    public Feed getFeed() {
        return getFeedRef() == null ? null : getFeedRef().get();
    }
    
    public void setFeed(final Feed feed) {
        this.setFeedRef(feed == null ? null : Ref.create(feed));
    }

    public static SubscriptionBuilder builder() {
        return new SubscriptionBuilder();
    }

    public static class SubscriptionBuilder {

        private Long id;
        private Group group;
        private Feed feed;
        private String title;
        private int order;
        private Long lastUpdateDate;

        SubscriptionBuilder() {

        }

        public SubscriptionBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public SubscriptionBuilder group(final Group group) {
            this.group = group;
            return this;
        }

        public SubscriptionBuilder feed(final Feed feed) {
            this.feed = feed;
            return this;
        }

        public SubscriptionBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public SubscriptionBuilder order(final int order) {
            this.order = order;
            return this;
        }

        public SubscriptionBuilder lastUpdateDate(final Long lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }
        
        public Subscription build() {
            return new Subscription(this);
        }

    }

}
