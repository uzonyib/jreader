package jreader.services;

import java.util.List;
import java.util.Map;

import jreader.dto.PostDto;

public interface PostService {

    void markRead(String username, Map<Long, Map<Long, List<Long>>> ids);

    void star(String username, Long groupId, Long subscriptionId, Long postId);

    void unstar(String username, Long groupId, Long subscriptionId, Long postId);

    List<PostDto> list(PostFilterData filterData);

}
