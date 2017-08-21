package jreader.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jreader.domain.FeedStat;
import jreader.dto.FeedStatDto;

@Component
public class FeedStatDtoConverter implements Converter<FeedStat, FeedStatDto> {

    @Override
    public FeedStatDto convert(final FeedStat entity) {
        return new FeedStatDto(entity.getRefreshDate(), entity.getCount());
    }

}
