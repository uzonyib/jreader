package jreader.converter;

import org.springframework.core.convert.converter.Converter;

import jreader.domain.FeedStat;
import jreader.dto.FeedStatDto;

public class FeedStatDtoConverter implements Converter<FeedStat, FeedStatDto> {

    @Override
    public FeedStatDto convert(final FeedStat entity) {
        final FeedStatDto dto = new FeedStatDto();
        dto.setDate(entity.getRefreshDate());
        dto.setCount(entity.getCount());
        return dto;
    }

}
