package jreader.feed.api.service.impl;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;

import jreader.feed.model.Feed;

@Service
public class FeedConverter implements Converter<Feed, SyndFeed> {

    @Override
    public SyndFeed convert(final Feed feed) {
        final SyndFeed syndFeed = new SyndFeedImpl();
        syndFeed.setTitle(feed.getTitle());
        syndFeed.setDescription(feed.getDescription());
        return syndFeed;
    }

}
