package jreader.rss.impl;

import java.net.URL;

import jreader.rss.RssService;
import jreader.rss.domain.Feed;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Service
public class RssServiceImpl implements RssService {

	@Autowired
	@Qualifier("rssMapper")
	private DozerBeanMapper mapper;

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
//				System.out.println(syndEntry.getPublishedDate());
//			}
			return feed;
		} catch (Exception e) {
			return null;
		}
	}

}
