package jreader.services.impl;

import java.net.URL;

import jreader.dto.RssFetchResult;
import jreader.services.FeedReader;

import org.springframework.core.convert.ConversionService;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RomeFeedReader implements FeedReader {

	private ConversionService conversionService;
	
	public RomeFeedReader(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public RssFetchResult fetch(URL url) throws Exception {
		SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(url));
		return conversionService.convert(syndFeed, RssFetchResult.class);
	}

}
