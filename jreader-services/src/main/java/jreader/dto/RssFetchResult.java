package jreader.dto;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedEntry;

public class RssFetchResult {

    private Feed feed;
    private List<FeedEntry> feedEntries;

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public List<FeedEntry> getFeedEntries() {
        return feedEntries;
    }

    public void setFeedEntries(List<FeedEntry> feedEntries) {
        this.feedEntries = feedEntries;
    }

}
