package jreader.dto;

import java.util.List;

import lombok.Data;

@Data
public class GroupDto {

    private String id;
    private String title;
    private List<SubscriptionDto> subscriptions;
    private int unreadCount;
    private int order;

}
