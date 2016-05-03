package jreader.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.appengine.api.users.UserServiceFactory;

import jreader.services.ArchiveService;
import jreader.services.CronService;
import jreader.services.GroupService;
import jreader.services.PostService;
import jreader.services.StatService;
import jreader.services.SubscriptionService;
import jreader.services.UserAdminService;
import jreader.services.UserService;
import jreader.services.impl.ArchiveServiceImpl;
import jreader.services.impl.CronServiceImpl;
import jreader.services.impl.GroupServiceImpl;
import jreader.services.impl.PostServiceImpl;
import jreader.services.impl.StatServiceImpl;
import jreader.services.impl.SubscriptionServiceImpl;
import jreader.services.impl.UserAdminServiceImpl;
import jreader.services.impl.UserServiceImpl;

@Configuration
@Import({ DaoConfig.class, UtilConfig.class })
public class ServiceConfig {
    
    @Autowired
    private DaoConfig daoConfig;
    
    @Autowired
    private UtilConfig utilConfig;
    
    @Bean
    public com.google.appengine.api.users.UserService googleUserService() {
        return UserServiceFactory.getUserService();
    }
    
    @Bean
    public UserService userService() {
        return new UserServiceImpl(daoConfig.userDao(), utilConfig.dateHelper());
    }
    
    @Bean
    public GroupService groupService() {
        return new GroupServiceImpl(daoConfig.daoFacade(), utilConfig.conversionService(), utilConfig.builderFactory());
    }
    
    @Bean
    public SubscriptionService subscriptionService() {
        return new SubscriptionServiceImpl(daoConfig.daoFacade(), utilConfig.rssService(), utilConfig.conversionService(), utilConfig.builderFactory());
    }
    
    @Bean
    public PostService postService() {
        return new PostServiceImpl(daoConfig.daoFacade(), utilConfig.conversionService());
    }
    
    @Bean
    public StatService statService() {
        return new StatServiceImpl(daoConfig.daoFacade(), utilConfig.conversionService(), utilConfig.dateHelper());
    }
    
    @Bean
    public ArchiveService archiveService() {
        return new ArchiveServiceImpl(daoConfig.daoFacade(), utilConfig.conversionService(), utilConfig.builderFactory());
    }
    
    @Bean
    public CronService cronService() {
        return new CronServiceImpl(daoConfig.daoFacade(), utilConfig.rssService(), utilConfig.conversionService(), utilConfig.builderFactory(),
                utilConfig.dateHelper());
    }
    
    @Bean
    public UserAdminService userAdminService() {
        return new UserAdminServiceImpl(daoConfig.userDao(), utilConfig.conversionService());
    }

}
