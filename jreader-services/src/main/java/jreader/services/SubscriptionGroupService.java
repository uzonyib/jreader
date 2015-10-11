package jreader.services;

import java.util.List;

import jreader.dto.SubscriptionGroupDto;

public interface SubscriptionGroupService {
    
    List<SubscriptionGroupDto> list(String username);

    SubscriptionGroupDto create(String username, String title);
    
    void entitle(String username, Long subscriptionGroupId, String title);

    void moveUp(String username, Long subscriptionGroupId);

    void moveDown(String username, Long subscriptionGroupId);
    
    void delete(String username, Long subscriptionGroupId);

}
