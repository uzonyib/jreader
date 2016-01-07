package jreader.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import jreader.web.controller.AdminController;
import jreader.web.controller.PageController;
import jreader.web.controller.ajax.ArchiveController;
import jreader.web.controller.ajax.ArchivedPostController;
import jreader.web.controller.ajax.PostController;
import jreader.web.controller.ajax.GlobalExceptionHandler;
import jreader.web.controller.ajax.GroupController;
import jreader.web.controller.ajax.StatController;
import jreader.web.controller.ajax.SubscriptionController;
import jreader.web.controller.appengine.CronJobController;
import jreader.web.controller.appengine.TaskController;
import jreader.web.service.QueueService;
import jreader.web.service.impl.AppengineQueueService;

@Configuration
@Import(ServiceConfig.class)
@PropertySource({ "classpath:cron.properties", "classpath:application.properties" })
public class ControllerConfig extends WebMvcConfigurerAdapter {
    
    @Autowired
    private ServiceConfig serviceConfig;
    
    @Value("${version}")
    private String appVersion;
    
    @Value("${daysToKeepPosts}")
    private int daysToKeepPosts;
    
    @Value("${postsToKeep}")
    private int postsToKeep;
    
    @Value("${daysToDisplayStats}")
    private int daysToDisplayStats;
    
    @Value("${daysToKeepStats}")
    private int daysToKeepStats;
    
    @Value("${admin.users.pageSize}")
    private int userAdminPageSize;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public PageController pageController() {
        return new PageController(serviceConfig.googleUserService(), appVersion);
    }
    
    @Bean
    public QueueService queueService() {
        return new AppengineQueueService();
    }
    
    @Bean
    public CronJobController cronJobController() {
        return new CronJobController(queueService(), serviceConfig.googleUserService());
    }
    
    @Bean
    public TaskController taskController() {
        return new TaskController(serviceConfig.cronService(), queueService(), serviceConfig.googleUserService(), daysToKeepPosts, postsToKeep,
                daysToKeepStats);
    }
    
    @Bean
    public AdminController adminController() {
        return new AdminController(serviceConfig.userAdminService(), userAdminPageSize);
    }
    
    @Bean
    public GroupController groupController() {
        return new GroupController(serviceConfig.groupService());
    }
    
    @Bean
    public SubscriptionController subscriptionController() {
        return new SubscriptionController(serviceConfig.groupService(), serviceConfig.subscriptionService(), queueService());
    }
    
    @Bean
    public PostController postController() {
        return new PostController(serviceConfig.groupService(), serviceConfig.postService());
    }
    
    @Bean
    public StatController statController() {
        return new StatController(serviceConfig.statService(), daysToDisplayStats);
    }
    
    @Bean
    public ArchiveController archiveController() {
        return new ArchiveController(serviceConfig.archiveService());
    }
    
    @Bean
    public ArchivedPostController archivedPostController() {
        return new ArchivedPostController(serviceConfig.archiveService());
    }
    
    @Bean
    public GlobalExceptionHandler globalEnceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
