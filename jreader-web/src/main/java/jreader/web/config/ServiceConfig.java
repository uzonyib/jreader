package jreader.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.appengine.api.users.UserServiceFactory;

@Configuration
@Import({ DaoConfig.class, UtilConfig.class })
@ComponentScan("jreader.services")
public class ServiceConfig {

    @Bean
    public com.google.appengine.api.users.UserService googleUserService() {
        return UserServiceFactory.getUserService();
    }

}
