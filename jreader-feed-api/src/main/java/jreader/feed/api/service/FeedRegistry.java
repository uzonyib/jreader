package jreader.feed.api.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jreader.feed.model.Feed;
import jreader.feed.model.Post;

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
        if (post.getPublishDate() == null) {
            post.setPublishDate(System.currentTimeMillis());
        }
        feeds.get(getFeed(feedTitle)).add(post);
    }

    public List<Post> listPosts(final String feed) {
        return feeds.get(getFeed(feed));
    }

    public Feed getFeed(final String title) {
        for (final Feed feed : feeds.keySet()) {
            if (feed.getTitle().equals(title)) {
                return feed;
            }
        }
        return null;
    }

}
