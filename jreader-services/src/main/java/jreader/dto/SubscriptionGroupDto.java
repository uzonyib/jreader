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

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
