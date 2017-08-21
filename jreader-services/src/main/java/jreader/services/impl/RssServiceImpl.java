package jreader.services.impl;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dto.RssFetchResult;
import jreader.services.FeedFetcher;
import jreader.services.RssService;

@Service
public class RssServiceImpl implements RssService {

    private static final Logger LOG = Logger.getLogger(RssServiceImpl.class.getName());

    private FeedFetcher feedFetcher;
    private ConversionService conversionService;

    @Autowired
    public RssServiceImpl(final FeedFetcher feedFetcher, final ConversionService conversionService) {
        this.feedFetcher = feedFetcher;
        this.conversionService = conversionService;
    }

    @Override
    public RssFetchResult fetch(final String url) {
        try {
            final URL feedSource = new URL(url);
            final SyndFeed syndFeed = feedFetcher.retrieveFeed(feedSource);
            final RssFetchResult result = conversionService.convert(syndFeed, RssFetchResult.class);
            if (result != null && result.getFeed() != null) {
                result.getFeed().setUrl(url);
            }
            return result;
        } catch (final Exception e) {
            LOG.log(Level.WARNING, "Error while fetching feed: " + url, e);
            return null;
        }
    }

}
