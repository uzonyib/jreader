package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class FeedStat {

    @Id
    private Long id;
    @Load
    @Parent
    private Ref<Feed> feedRef;
    private Long refreshDate;
    private int count;
    
    public FeedStat() {
        
    }

    private FeedStat(final Builder builder) {
        this.setFeed(builder.feed);
        this.setRefreshDate(builder.refreshDate);
        this.setCount(builder.count);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Ref<Feed> getFeedRef() {
        return feedRef;
    }

    public void setFeedRef(final Ref<Feed> feedRef) {
        this.feedRef = feedRef;
    }

    public Feed getFeed() {
        return getFeedRef() == null ? null : getFeedRef().get();
    }

    public void setFeed(final Feed feed) {
        this.setFeedRef(feed == null ? null : Ref.create(feed));
    }

    public Long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(final Long refreshDate) {
        this.refreshDate = refreshDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public static class Builder {

        private Feed feed;
        private Long refreshDate;
        private int count;

        public Builder feed(final Feed feed) {
            this.feed = feed;
            return this;
        }

        public Builder refreshDate(final Long refreshDate) {
            this.refreshDate = refreshDate;
            return this;
        }

        public Builder count(final int count) {
            this.count = count;
            return this;
        }
        
        public FeedStat build() {
            return new FeedStat(this);
        }

    }

}
