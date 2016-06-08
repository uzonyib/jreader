package jreader.dto;

import lombok.Data;

@Data
public class SubscriptionDto {

    private String id;
    private String title;
    private FeedDto feed;
    private int unreadCount;
    private Long lastUpdateDate;
    private int order;

}
