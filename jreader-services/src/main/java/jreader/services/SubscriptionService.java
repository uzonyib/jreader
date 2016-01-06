package jreader.services;

import jreader.dto.SubscriptionDto;

public interface SubscriptionService {

    SubscriptionDto subscribe(String username, Long groupId, String url);

    void unsubscribe(String username, Long groupId, Long subscriptionId);
    
    void entitle(String username, Long groupId, Long subscriptionId, String title);

    void moveUp(String username, Long groupId, Long subscriptionId);

    void moveDown(String username, Long groupId, Long subscriptionId);

}
