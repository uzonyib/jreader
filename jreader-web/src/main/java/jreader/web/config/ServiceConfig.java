package jreader.web.config;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import com.google.appengine.api.users.UserServiceFactory;
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
import jreader.domain.BuilderFactory;
import jreader.services.ArchiveService;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.GroupService;
import jreader.services.PostService;
import jreader.services.RssService;
import jreader.services.StatService;
import jreader.services.SubscriptionService;
import jreader.services.UserAdminService;
import jreader.services.UserService;
import jreader.services.impl.ArchiveServiceImpl;
import jreader.services.impl.CronServiceImpl;
import jreader.services.impl.DateHelperImpl;
import jreader.services.impl.GroupServiceImpl;
import jreader.services.impl.PostServiceImpl;
import jreader.services.impl.RssServiceImpl;
import jreader.services.impl.StatServiceImpl;
import jreader.services.impl.SubscriptionServiceImpl;
import jreader.services.impl.UserAdminServiceImpl;
import jreader.services.impl.UserServiceImpl;

@Configuration
@Import(DaoConfig.class)
public class ServiceConfig {
    
    @Autowired
    private DaoConfig daoConfig;
    
    @Bean
    public com.google.appengine.api.users.UserService googleUserService() {
        return UserServiceFactory.getUserService();
    }
    
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
        return new UserServiceImpl(daoConfig.userDao(), dateHelper());
    }
    
    @Bean
    public GroupService groupService() {
        return new GroupServiceImpl(daoConfig.userDao(), daoConfig.groupDao(), daoConfig.subscriptionDao(), daoConfig.postDao(),
                conversionService(), builderFactory());
    }
    
    @Bean
    public SubscriptionService subscriptionService() {
        return new SubscriptionServiceImpl(daoConfig.userDao(), daoConfig.groupDao(), daoConfig.subscriptionDao(), daoConfig.feedDao(),
                daoConfig.postDao(), rssService(), conversionService(), builderFactory());
    }
    
    @Bean
    public PostService postService() {
        return new PostServiceImpl(daoConfig.userDao(), daoConfig.groupDao(), daoConfig.subscriptionDao(), daoConfig.postDao(),
                conversionService());
    }
    
    @Bean
    public StatService statService() {
        return new StatServiceImpl(daoConfig.userDao(), daoConfig.groupDao(), daoConfig.subscriptionDao(), daoConfig.feedStatDao(),
                conversionService(), dateHelper());
    }
    
    @Bean
    public ArchiveService archiveService() {
        return new ArchiveServiceImpl(daoConfig.userDao(), daoConfig.groupDao(), daoConfig.subscriptionDao(), daoConfig.postDao(),
                daoConfig.archiveDao(), daoConfig.archivedPostDao(), conversionService(), builderFactory());
    }
    
    @Bean
    public CronService cronService() {
        return new CronServiceImpl(daoConfig.subscriptionDao(), daoConfig.feedDao(), daoConfig.postDao(), daoConfig.feedStatDao(), rssService(),
                conversionService(), builderFactory(), dateHelper());
    }
    
    @Bean
    public UserAdminService userAdminService() {
        return new UserAdminServiceImpl(daoConfig.userDao(), conversionService());
    }

}
