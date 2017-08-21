package jreader.services.impl;

import java.net.URL;

import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jreader.services.FeedFetcher;

@Component
public class FeedFetcherImpl implements FeedFetcher {

    @Override
    public SyndFeed retrieveFeed(final URL url) throws Exception {
        return new SyndFeedInput().build(new XmlReader(url));
    }

}
