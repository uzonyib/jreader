package jreader.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import jreader.web.controller.PageController;
import jreader.web.controller.ajax.ArchiveController;
import jreader.web.controller.ajax.ArchivedEntryController;
import jreader.web.controller.ajax.EntryController;
import jreader.web.controller.ajax.GlobalExceptionHandler;
import jreader.web.controller.ajax.GroupController;
import jreader.web.controller.ajax.SubscriptionController;
import jreader.web.controller.appengine.CronJobController;
import jreader.web.controller.appengine.TaskController;

@Configuration
@Import(ServiceConfig.class)
@PropertySource({ "classpath:cron.properties", "classpath:application.properties" })
public class ControllerConfig extends WebMvcConfigurerAdapter {
    
    @Autowired
    private ServiceConfig serviceConfig;
    
    @Value("${version}")
    private String appVersion;
    
    @Value("${minAgeToDelete}")
    private int minAgeToDelete;
    
    @Value("${minCountToKeep}")
    private int minCountToKeep;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public UserService googleUserService() {
        return UserServiceFactory.getUserService();
    }
    
    @Bean
    public PageController pageController() {
        return new PageController(serviceConfig.userService(), googleUserService(), appVersion);
    }
    
    @Bean
    public CronJobController cronJobController() {
        return new CronJobController();
    }
    
    @Bean
    public TaskController taskController() {
        return new TaskController(serviceConfig.cronService(), minAgeToDelete, minCountToKeep);
    }
    
    @Bean
    public GroupController groupController() {
        return new GroupController(serviceConfig.subscriptionService());
    }
    
    @Bean
    public SubscriptionController subscriptionController() {
        return new SubscriptionController(serviceConfig.subscriptionService());
    }
    
    @Bean
    public EntryController entryController() {
        return new EntryController(serviceConfig.subscriptionService(), serviceConfig.feedEntryService());
    }
    
    @Bean
    public ArchiveController archiveController() {
        return new ArchiveController(serviceConfig.archiveService());
    }
    
    @Bean
    public ArchivedEntryController archivedEntryController() {
        return new ArchivedEntryController(serviceConfig.archiveService());
    }
    
    @Bean
    public GlobalExceptionHandler globalEnceptionHandler() {
        return new GlobalExceptionHandler();
    }

}