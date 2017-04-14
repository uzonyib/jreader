package jreader.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class GroupDto {

    private Long id;
    private String title;
    private int order;
    private List<SubscriptionDto> subscriptions;
    private int unreadCount;

}
