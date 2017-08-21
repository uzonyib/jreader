package jreader.converter;

import jreader.domain.Feed;
import jreader.dto.FeedDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FeedDtoConverter implements Converter<Feed, FeedDto> {

    @Override
    public FeedDto convert(final Feed entity) {
        return FeedDto.builder()
                .url(entity.getUrl())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .feedType(entity.getFeedType())
                .lastUpdateDate(entity.getLastUpdateDate())
                .lastRefreshDate(entity.getLastRefreshDate())
                .status(entity.getStatus())
                .build();
    }

}
