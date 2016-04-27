package jreader.services;

public class PostFilter extends jreader.dao.PostFilter {

    public enum Vertical {
        ALL, GROUP, SUBSCRIPTION;
    }

    private Vertical vertical;
    private String username;
    private Long groupId;
    private Long subscriptionId;

    public PostFilter(final String username, final PostType postType, final boolean ascending, final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.vertical = Vertical.ALL;
        this.username = username;
    }

    public PostFilter(final String username, final Long groupId, final PostType postType, final boolean ascending, final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.vertical = Vertical.GROUP;
        this.username = username;
        this.groupId = groupId;
    }

    public PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending,
            final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.vertical = Vertical.SUBSCRIPTION;
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
    }
    
    public Vertical getVertical() {
        return vertical;
    }
    
    public void setVertical(final Vertical type) {
        this.vertical = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
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
