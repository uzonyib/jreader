package jreader.feed.client;

import java.util.List;

import jreader.feed.model.Feed;
import jreader.feed.model.Post;

public interface FeedApiClient {

    void deleteAllFeeds();

    List<Feed> listFeeds();

    void addFeed(Feed feed);

    List<Post> listPosts(String feed);

    void addPost(String feed, Post post);

    String listRssPosts(String feed);

}
