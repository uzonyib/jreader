package jreader.dto;

import java.util.List;

public class SubscriptionGroupDto {

    private String id;
    private String title;
    private List<SubscriptionDto> subscriptions;
    private int unreadCount;
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

    public List<SubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final List<SubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(final int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

}
