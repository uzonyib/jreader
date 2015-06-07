package jreader.services;

import java.util.List;

import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;

public interface SubscriptionService {

    SubscriptionGroupDto createGroup(String username, String title);

    SubscriptionDto subscribe(String username, Long subscriptionGroupId, String url);

    void unsubscribe(String username, Long subscriptionGroupId, Long subscriptionId);

    void deleteGroup(String username, Long subscriptionGroupId);

    void moveUp(String username, Long subscriptionGroupId);

    void moveDown(String username, Long subscriptionGroupId);

    void moveUp(String username, Long subscriptionGroupId, Long subscriptionId);

    void moveDown(String username, Long subscriptionGroupId, Long subscriptionId);

    List<SubscriptionGroupDto> list(String username);

    void entitle(String username, Long subscriptionGroupId, Long subscriptionId, String title);

    void entitle(String username, Long subscriptionGroupId, String title);

}
