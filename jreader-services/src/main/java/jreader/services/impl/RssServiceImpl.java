package jreader.services.impl;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import jreader.dto.RssFetchResult;
import jreader.services.RssService;

import org.springframework.core.convert.ConversionService;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssServiceImpl implements RssService {
	
	private static final Logger LOG = Logger.getLogger(RssServiceImpl.class.getName());

	private ConversionService conversionService;
	
	public RssServiceImpl(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public RssFetchResult fetch(String url) {
		try {
			URL feedSource = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed syndFeed = input.build(new XmlReader(feedSource));
			RssFetchResult result = conversionService.convert(syndFeed, RssFetchResult.class);
			result.getFeed().setUrl(url);
			return result;
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Error while fetching feed: " + url, e);
			return null;
		}
	}
	
}
