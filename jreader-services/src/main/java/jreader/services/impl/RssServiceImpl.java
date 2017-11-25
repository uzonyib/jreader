package jreader.services.impl;

import static java.util.Objects.nonNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndFeed;

import jreader.dto.RssFetchResult;
import jreader.services.FeedFetcher;
import jreader.services.RssService;

@Service
public class RssServiceImpl implements RssService {

    private FeedFetcher feedFetcher;
    private ConversionService conversionService;

    @Autowired
    public RssServiceImpl(final FeedFetcher feedFetcher, final ConversionService conversionService) {
        this.feedFetcher = feedFetcher;
        this.conversionService = conversionService;
    }

    @Override
    public RssFetchResult fetch(final String url) {
        final URL feedSource = getSource(url);
        final SyndFeed syndFeed = feedFetcher.retrieveFeed(feedSource);
        return convertResult(url, syndFeed);
    }

    private URL getSource(final String url) {
        try {
            return new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Invalid feed URL: " + url);
        }
    }

    private RssFetchResult convertResult(final String url, final SyndFeed syndFeed) {
        final RssFetchResult result = conversionService.convert(syndFeed, RssFetchResult.class);
        if (nonNull(result) && nonNull(result.getFeed())) {
            result.getFeed().setUrl(url);
        }
        return result;
    }

}
