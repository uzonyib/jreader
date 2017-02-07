package jreader.web.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.rometools.fetcher.impl.HttpURLFeedFetcher;

import jreader.converter.ArchiveDtoConverter;
import jreader.converter.ArchivedPostConverter;
import jreader.converter.ArchivedPostDtoConverter;
import jreader.converter.ConversionFactory;
import jreader.converter.FeedDtoConverter;
import jreader.converter.FeedStatDtoConverter;
import jreader.converter.GroupDtoConverter;
import jreader.converter.PostDtoConverter;
import jreader.converter.RssFetchResultConverter;
import jreader.converter.SubscriptionDtoConverter;
import jreader.converter.UserDtoConverter;
import jreader.services.DateHelper;
import jreader.services.RssService;
import jreader.services.impl.DateHelperImpl;
import jreader.services.impl.RssServiceImpl;

@Configuration
@Import(DaoConfig.class)
public class UtilConfig {

    @Bean
    public ConversionFactory conversionFactory() {
        final Set<Converter<?, ?>> converters = new LinkedHashSet<>();
        converters.add(new RssFetchResultConverter());
        converters.add(new FeedDtoConverter());
        converters.add(new PostDtoConverter());
        converters.add(new FeedStatDtoConverter());
        converters.add(new UserDtoConverter());
        converters.add(new GroupDtoConverter());
        converters.add(new SubscriptionDtoConverter());
        converters.add(new ArchiveDtoConverter());
        converters.add(new ArchivedPostConverter());
        converters.add(new ArchivedPostDtoConverter());
        return new ConversionFactory(converters);
    }

    @Bean
    public ConversionService conversionService() {
        return conversionFactory().getObject();
    }

    @Bean
    public RssService rssService() {
        return new RssServiceImpl(new HttpURLFeedFetcher(), conversionService());
    }

    @Bean
    public DateHelper dateHelper() {
        return new DateHelperImpl();
    }

}
