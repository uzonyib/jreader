package jreader.converter;

import java.util.ArrayList;
import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.dto.RssFetchResult;

import org.springframework.core.convert.converter.Converter;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class RssFetchResultConverter implements Converter<SyndFeed, RssFetchResult> {
	
	@Override
	public RssFetchResult convert(SyndFeed syndFeed) {
		RssFetchResult result = new RssFetchResult();
		result.setFeed(new Feed());
		result.setFeedEntries(new ArrayList<FeedEntry>());
		
		result.getFeed().setTitle(syndFeed.getTitle());
		result.getFeed().setDescription(syndFeed.getDescription());
		result.getFeed().setFeedType(syndFeed.getFeedType());
		if (syndFeed.getPublishedDate() != null) {
			result.getFeed().setPublishedDate(syndFeed.getPublishedDate().getTime());
		}
		if (syndFeed.getEntries() != null) {
			for (SyndEntry syndEntry : (List<SyndEntry>) syndFeed.getEntries()) {
				result.getFeedEntries().add(convert(syndEntry));
			}
		}
		return result;
	}
	
	private static FeedEntry convert(SyndEntry syndEntry) {
		FeedEntry feedEntry = new FeedEntry();
		feedEntry.setTitle(syndEntry.getTitle());
		if (syndEntry.getDescription() != null) {
			feedEntry.setDescription(syndEntry.getDescription().getValue());
		}
		feedEntry.setAuthor(syndEntry.getAuthor());
		feedEntry.setLink(syndEntry.getLink());
		if (syndEntry.getPublishedDate() != null) {
			feedEntry.setPublishedDate(syndEntry.getPublishedDate().getTime());
		} else if (syndEntry.getUpdatedDate() != null) {
			feedEntry.setPublishedDate(syndEntry.getUpdatedDate().getTime());
		}
		return feedEntry;
	}

}
