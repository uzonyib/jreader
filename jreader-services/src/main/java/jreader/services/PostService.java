package jreader.services;

import java.util.List;
import java.util.Map;

import jreader.dto.PostDto;

public interface PostService {

    void markRead(String username, Map<Long, Map<Long, List<Long>>> ids);

    void bookmark(String username, Long groupId, Long subscriptionId, Long postId);

    void deleteBookmark(String username, Long groupId, Long subscriptionId, Long postId);

    List<PostDto> list(PostFilter filter);

}
