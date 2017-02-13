package jreader.feed.server.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;

@Service
public class FeedRegistry {

    private Map<Feed, List<Post>> feeds = new LinkedHashMap<>();

    public List<Feed> listFeeds() {
        return new ArrayList<Feed>(feeds.keySet());
    }

    public void add(final Feed feed) {
        feeds.put(feed, new ArrayList<Post>());
    }

    public void reset() {
        feeds = new LinkedHashMap<>();
    }

    public void add(final String feedTitle, final Post post) {
        feeds.get(new Feed(feedTitle)).add(post);
    }

    public List<Post> listPosts(final String feed) {
        return feeds.get(new Feed(feed));
    }

}
