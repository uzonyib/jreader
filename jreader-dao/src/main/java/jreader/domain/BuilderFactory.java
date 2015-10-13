package jreader.domain;

import jreader.domain.FeedStat.Builder;

public class BuilderFactory {
    
    public SubscriptionGroup.Builder createGroupBuilder() {
        return new SubscriptionGroup.Builder();
    }

    public Subscription.Builder createSubscriptionBuilder() {
        return new Subscription.Builder();
    }

    public Archive.Builder createArchiveBuilder() {
        return new Archive.Builder();
    }
    
    public Builder createFeedStatBuilder() {
        return new FeedStat.Builder();
    }

}
