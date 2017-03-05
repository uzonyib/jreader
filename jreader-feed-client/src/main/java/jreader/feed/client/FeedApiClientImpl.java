package jreader.feed.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import jreader.feed.model.Feed;
import jreader.feed.model.Post;

public class FeedApiClientImpl implements FeedApiClient {

    private static final String FEEDS_URL = "/feeds";
    private static final String FEED_URL = FEEDS_URL + "/{feed}";
    private static final String RSS_URL = FEED_URL + "/rss";

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public FeedApiClientImpl(final String baseUrl, final RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public void deleteAllFeeds() {
        restTemplate.delete(baseUrl + FEEDS_URL);
    }

    @Override
    public List<Feed> listFeeds() {
        return Arrays.asList(restTemplate.getForObject(baseUrl + FEEDS_URL, Feed[].class));
    }

    @Override
    public void addFeed(final Feed feed) {
        restTemplate.put(baseUrl + FEEDS_URL, feed);
    }

    @Override
    public List<Post> listPosts(final String feed) {
        return Arrays.asList(restTemplate.getForObject(baseUrl + FEED_URL, Post[].class, feed));
    }

    @Override
    public void addPost(final String feed, final Post post) {
        restTemplate.put(baseUrl + FEED_URL, post, feed);
    }

    @Override
    public String listRssPosts(final String feed) {
        return restTemplate.getForObject(baseUrl + RSS_URL, String.class, feed);
    }

}
