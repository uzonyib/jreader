package jreader.domain;

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

}
