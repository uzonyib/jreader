package jreader.rss.impl;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import jreader.rss.RssService;
import jreader.rss.domain.Feed;

import org.dozer.DozerBeanMapper;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssServiceImpl implements RssService {
	
	private static final Logger LOG = Logger.getLogger(RssServiceImpl.class.getName());

	private DozerBeanMapper mapper;

	public RssServiceImpl(DozerBeanMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Feed fetch(String url) {
		try {
			URL feedSource = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed syndFeed = input.build(new XmlReader(feedSource));
			Feed feed = mapper.map(syndFeed, Feed.class);
			feed.setUrl(url);
			
//			@SuppressWarnings("unchecked")
//			List<SyndEntry> syndEntries = syndFeed.getEntries();
//			for (SyndEntry syndEntry : syndEntries) {
//				FeedEntry feedEntry = new FeedEntry();
//				System.out.println(syndEntry.getDescription().getValue());
//			}
			return feed;
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Error while fetching feed.", e);
			return null;
		}
	}

}
