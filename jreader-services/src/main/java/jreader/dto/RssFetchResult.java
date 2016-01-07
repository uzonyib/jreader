package jreader.dto;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Post;

public class RssFetchResult {

    private Feed feed;
    private List<Post> posts;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(final Feed feed) {
        this.feed = feed;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(final List<Post> posts) {
        this.posts = posts;
    }

}
