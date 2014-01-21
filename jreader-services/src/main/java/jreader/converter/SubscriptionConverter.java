package jreader.converter;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.SubscriptionDto;

import org.springframework.core.convert.converter.Converter;

public class SubscriptionConverter implements Converter<Subscription, SubscriptionDto> {

	@Override
	public SubscriptionDto convert(Subscription entity) {
		SubscriptionDto dto = new SubscriptionDto();
		dto.setId(String.valueOf(entity.getId()));
		dto.setTitle(entity.getTitle());
		dto.setUpdatedDate(entity.getUpdatedDate());
		dto.setRefreshDate(entity.getRefreshDate());
		dto.setOrder(entity.getOrder());
		dto.setFeed(convert(entity.getFeed()));
		return dto;
	}
	
	private static FeedDto convert(Feed entity) {
		FeedDto dto = new FeedDto();
		dto.setTitle(entity.getTitle());
		dto.setUrl(entity.getUrl());
		dto.setDescription(entity.getDescription());
		dto.setFeedType(entity.getFeedType());
		dto.setPublishedDate(entity.getPublishedDate());
		return dto;
	}

}
