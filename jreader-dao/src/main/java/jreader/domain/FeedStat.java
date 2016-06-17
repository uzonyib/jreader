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
public class FeedStat {

    @Id
    private Long id;
    @Load
    @Parent
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<Feed> feedRef;
    @Index
    private Long refreshDate;
    private int count;
    
    public FeedStat(final Feed feed, final Long refreshDate, final int count) {
        this.setFeed(feed);
        this.refreshDate = refreshDate;
        this.count = count;
    }

    private FeedStat(final Builder builder) {
        this.setFeed(builder.feed);
        this.setRefreshDate(builder.refreshDate);
        this.setCount(builder.count);
    }
    
    public Feed getFeed() {
        return getFeedRef() == null ? null : getFeedRef().get();
    }
    
    public void setFeed(final Feed feed) {
        this.setFeedRef(feed == null ? null : Ref.create(feed));
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
