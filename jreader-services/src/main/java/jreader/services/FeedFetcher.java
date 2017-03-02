package jreader.services;

import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;

public interface FeedFetcher {

    SyndFeed retrieveFeed(URL feedSource) throws Exception;

}
