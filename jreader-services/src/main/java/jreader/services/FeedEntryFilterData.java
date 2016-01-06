package jreader.services;

import jreader.dao.FeedEntryFilter;

public class FeedEntryFilterData extends FeedEntryFilter {

    public enum Type {
        ALL, GROUP, SUBSCRIPTION;
    }

    private String username;
    private Type type;
    private Long groupId;
    private Long subscriptionId;

    public FeedEntryFilterData(final String username, final Selection selection, final boolean ascending, final int offset, final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.type = Type.ALL;
    }

    public FeedEntryFilterData(final String username, final Long groupId, final Selection selection, final boolean ascending, final int offset,
            final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.groupId = groupId;
        this.type = Type.GROUP;
    }

    public FeedEntryFilterData(final String username, final Long groupId, final Long subscriptionId, final Selection selection, final boolean ascending,
            final int offset, final int count) {
        super(selection, ascending, offset, count);
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
        this.type = Type.SUBSCRIPTION;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(final Long groupId) {
        this.groupId = groupId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
