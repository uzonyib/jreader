package jreader.dto;

import java.util.List;

import lombok.Data;

@Data
public final class GroupDto {

    private final String id;
    private final String title;
    private final int order;
    private List<SubscriptionDto> subscriptions;
    private int unreadCount;

}
