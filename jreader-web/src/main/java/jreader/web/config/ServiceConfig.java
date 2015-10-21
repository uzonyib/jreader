package jreader.web.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.rometools.fetcher.impl.HttpURLFeedFetcher;

import jreader.converter.ArchiveDtoConverter;
import jreader.converter.ArchivedEntryConverter;
import jreader.converter.ArchivedEntryDtoConverter;
import jreader.converter.ConversionFactory;
import jreader.converter.FeedDtoConverter;
import jreader.converter.FeedEntryDtoConverter;
import jreader.converter.FeedStatDtoConverter;
import jreader.converter.RssFetchResultConverter;
import jreader.converter.SubscriptionDtoConverter;
import jreader.converter.SubscriptionGroupDtoConverter;
import jreader.domain.BuilderFactory;
import jreader.services.ArchiveService;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.FeedEntryService;
import jreader.services.RssService;
import jreader.services.StatService;
import jreader.services.SubscriptionGroupService;
import jreader.services.SubscriptionService;
import jreader.services.UserService;
import jreader.services.impl.ArchiveServiceImpl;
import jreader.services.impl.CronServiceImpl;
import jreader.services.impl.DateHelperImpl;
import jreader.services.impl.FeedEntryServiceImpl;
import jreader.services.impl.RssServiceImpl;
import jreader.services.impl.StatServiceImpl;
import jreader.services.impl.SubscriptionGroupServiceImpl;
import jreader.services.impl.SubscriptionServiceImpl;
import jreader.services.impl.UserServiceImpl;

@Configuration
@Import(DaoConfig.class)
public class ServiceConfig {
    
    @Autowired
    private DaoConfig daoConfig;
    
    @Bean
    public ConversionFactory conversionFactory() {
        final Set<Converter<?, ?>> converters = new LinkedHashSet<>();
        converters.add(new RssFetchResultConverter());
        converters.add(new FeedDtoConverter());
        converters.add(new FeedEntryDtoConverter());
        converters.add(new FeedStatDtoConverter());
        converters.add(new SubscriptionGroupDtoConverter());
        converters.add(new SubscriptionDtoConverter());
        converters.add(new ArchiveDtoConverter());
        converters.add(new ArchivedEntryConverter());
        converters.add(new ArchivedEntryDtoConverter());
        return new ConversionFactory(converters);
    }
    
    @Bean
    public ConversionService conversionService() {
        return conversionFactory().getObject();
    }
    
    @Bean
    public BuilderFactory builderFactory() {
        return new BuilderFactory();
    }
    
    @Bean
    public RssService rssService() {
        return new RssServiceImpl(new HttpURLFeedFetcher(), conversionService());
    }
    
    @Bean
    public DateHelper dateHelper() {
        return new DateHelperImpl();
    }
    
    @Bean
    public UserService userService() {
        return new UserServiceImpl(daoConfig.userDao());
    }
    
    @Bean
    public SubscriptionGroupService subscriptionGroupService() {
        return new SubscriptionGroupServiceImpl(daoConfig.userDao(), daoConfig.subscriptionGroupDao(), daoConfig.subscriptionDao(), daoConfig.feedEntryDao(),
                conversionService(), builderFactory());
    }
    
    @Bean
    public SubscriptionService subscriptionService() {
        return new SubscriptionServiceImpl(daoConfig.userDao(), daoConfig.subscriptionGroupDao(), daoConfig.subscriptionDao(), daoConfig.feedDao(),
                daoConfig.feedEntryDao(), rssService(), conversionService(), builderFactory());
    }
    
    @Bean
    public FeedEntryService feedEntryService() {
        return new FeedEntryServiceImpl(daoConfig.userDao(), daoConfig.subscriptionGroupDao(), daoConfig.subscriptionDao(), daoConfig.feedEntryDao(),
                conversionService());
    }
    
    @Bean
    public StatService statService() {
        return new StatServiceImpl(daoConfig.userDao(), daoConfig.subscriptionGroupDao(), daoConfig.subscriptionDao(), daoConfig.feedStatDao(),
                conversionService(), dateHelper());
    }
    
    @Bean
    public ArchiveService archiveService() {
        return new ArchiveServiceImpl(daoConfig.userDao(), daoConfig.subscriptionGroupDao(), daoConfig.subscriptionDao(), daoConfig.feedEntryDao(),
                daoConfig.archiveDao(), daoConfig.archivedEntryDao(), conversionService(), builderFactory());
    }
    
    @Bean
    public CronService cronService() {
        return new CronServiceImpl(daoConfig.subscriptionDao(), daoConfig.feedDao(), daoConfig.feedEntryDao(), daoConfig.feedStatDao(), rssService(),
                conversionService(), builderFactory(), dateHelper());
    }

}
