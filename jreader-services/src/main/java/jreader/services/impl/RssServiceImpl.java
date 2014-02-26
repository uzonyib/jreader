package jreader.services.impl;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import jreader.dto.RssFetchResult;
import jreader.services.FeedReader;
import jreader.services.RssService;

public class RssServiceImpl implements RssService {
	
	private static final Logger LOG = Logger.getLogger(RssServiceImpl.class.getName());

	private FeedReader feedReader;
	
	public RssServiceImpl(FeedReader feedReader) {
		this.feedReader = feedReader;
	}

	@Override
	public RssFetchResult fetch(String url) {
		try {
			URL feedSource = new URL(url);
			RssFetchResult result = feedReader.fetch(feedSource);
			result.getFeed().setUrl(url);
			return result;
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Error while fetching feed: " + url, e);
			return null;
		}
	}
	
}
