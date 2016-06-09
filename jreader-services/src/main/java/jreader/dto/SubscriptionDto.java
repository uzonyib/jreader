package jreader.dto;

import lombok.Data;

@Data
public final class SubscriptionDto {

    private final String id;
    private final String title;
    private final FeedDto feed;
    private final Long lastUpdateDate;
    private final int order;
    private int unreadCount;

}
