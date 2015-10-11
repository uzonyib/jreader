package jreader.services;

import jreader.dto.SubscriptionDto;

public interface SubscriptionService {

    SubscriptionDto subscribe(String username, Long subscriptionGroupId, String url);

    void unsubscribe(String username, Long subscriptionGroupId, Long subscriptionId);
    
    void entitle(String username, Long subscriptionGroupId, Long subscriptionId, String title);

    void moveUp(String username, Long subscriptionGroupId, Long subscriptionId);

    void moveDown(String username, Long subscriptionGroupId, Long subscriptionId);

}
