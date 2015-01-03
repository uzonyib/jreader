package jreader.converter;

import jreader.domain.Feed;
import jreader.dto.FeedDto;

import org.springframework.core.convert.converter.Converter;

public class FeedDtoConverter implements Converter<Feed, FeedDto> {

	@Override
	public FeedDto convert(Feed entity) {
        FeedDto dto = new FeedDto();
        dto.setTitle(entity.getTitle());
        dto.setUrl(entity.getUrl());
        dto.setDescription(entity.getDescription());
        dto.setFeedType(entity.getFeedType());
        dto.setPublishedDate(entity.getPublishedDate());
        return dto;
    }

}
