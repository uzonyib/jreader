package jreader.services;

import java.util.List;
import java.util.Map;

import jreader.dto.FeedEntryDto;

public interface FeedEntryService {

    void markRead(String username, Map<Long, Map<Long, List<Long>>> ids);

    void star(String username, Long groupId, Long subscriptionId, Long feedEntryId);

    void unstar(String username, Long groupId, Long subscriptionId, Long feedEntryId);

    List<FeedEntryDto> listEntries(FeedEntryFilterData filterData);

}
