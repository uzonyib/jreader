package jreader.services;

import jreader.dao.FeedEntryFilter;

public class FeedEntryFilterData extends FeedEntryFilter {

    public static enum Group {
        ALL, SUBSCRIPTION_GROUP, SUBSCRIPTION;
    }

    private String username;
    private Group group;
    private Long subscriptionGroupId;
    private Long subscriptionId;

    public FeedEntryFilterData(final String username, final Selection selection, final boolean ascending, final int offset, final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.group = Group.ALL;
    }

    public FeedEntryFilterData(final String username, final Long subscriptionGroupId, final Selection selection, final boolean ascending, final int offset,
            final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.subscriptionGroupId = subscriptionGroupId;
        this.group = Group.SUBSCRIPTION_GROUP;
    }

    public FeedEntryFilterData(final String username, final Long subscriptionGroupId, final Long subscriptionId, final Selection selection,
            final boolean ascending, final int offset, final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.subscriptionGroupId = subscriptionGroupId;
        this.subscriptionId = subscriptionId;
        this.group = Group.SUBSCRIPTION;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(final Group group) {
        this.group = group;
    }

    public Long getSubscriptionGroupId() {
        return subscriptionGroupId;
    }

    public void setSubscriptionGroupId(final Long subscriptionGroupId) {
        this.subscriptionGroupId = subscriptionGroupId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
