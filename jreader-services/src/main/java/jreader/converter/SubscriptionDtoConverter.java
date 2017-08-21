package jreader.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.SubscriptionDto;

@Component
public class SubscriptionDtoConverter implements Converter<Subscription, SubscriptionDto>, ConversionServiceAware {

    private ConversionService conversionService;

    @Override
    public SubscriptionDto convert(final Subscription entity) {
        FeedDto feed = null;
        if (conversionService != null) {
            feed = conversionService.convert(entity.getFeed(), FeedDto.class);
        }
        return new SubscriptionDto(String.valueOf(entity.getId()), entity.getTitle(), feed, entity.getLastUpdateDate(), entity.getOrder());
    }

    @Override
    @Autowired
    @Lazy
    public void setConversionService(final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

}
