package jreader.dto;

public class SubscriptionDto {

    private String id;
    private String title;
    private FeedDto feed;
    private int unreadCount;
    private Long updatedDate;
    private int order;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public FeedDto getFeed() {
        return feed;
    }

    public void setFeed(final FeedDto feed) {
        this.feed = feed;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(final int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

}
