package jreader.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.SubscriptionDto;

public class SubscriptionDtoConverter implements Converter<Subscription, SubscriptionDto>, ConversionServiceAware {

    private ConversionService conversionService;

    @Override
    public SubscriptionDto convert(final Subscription entity) {
        final SubscriptionDto dto = new SubscriptionDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setTitle(entity.getTitle());
        dto.setLastUpdateDate(entity.getLastUpdateDate());
        dto.setOrder(entity.getOrder());
        if (conversionService != null) {
            dto.setFeed(conversionService.convert(entity.getFeed(), FeedDto.class));
        }
        return dto;
    }

    @Override
    public void setConversionService(final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

}
