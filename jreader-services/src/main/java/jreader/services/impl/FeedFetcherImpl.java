package jreader.services.impl;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jreader.services.FeedFetcher;
import jreader.services.exception.FetchException;

@Component
public class FeedFetcherImpl implements FeedFetcher {

    private static final Logger LOG = Logger.getLogger(FeedFetcherImpl.class.getName());

    @Override
    public SyndFeed retrieveFeed(final URL url) {
        try {
            return new SyndFeedInput().build(new XmlReader(url));
        } catch (final IllegalArgumentException | FeedException | IOException e) {
            LOG.log(Level.WARNING, "Error while fetching feed: " + url, e);
            throw new FetchException(e);
        }
    }

}
