package jreader.feed.api.service;

import java.util.List;

import jreader.feed.model.Feed;
import jreader.feed.model.Post;

public interface FeedRegistry {

    List<Feed> listFeeds();

    void add(Feed feed);

    void reset();

    void add(String feedTitle, Post post);

    List<Post> listPosts(String feed);

    Feed getFeed(String title);

}
