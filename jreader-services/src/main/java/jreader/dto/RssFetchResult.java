package jreader.dto;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Post;
import lombok.Value;

@Value
public class RssFetchResult {

    private Feed feed;
    private List<Post> posts;

}
