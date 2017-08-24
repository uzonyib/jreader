package jreader.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import jreader.domain.Role;
import jreader.services.UserService;
import jreader.web.interceptor.AuthorizationInterceptor;
import jreader.web.interceptor.CronJobInterceptor;
import jreader.web.interceptor.TaskInterceptor;

@Configuration
@EnableWebMvc
@Import(ControllerConfig.class)
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private com.google.appengine.api.users.UserService googleUserService;

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(
                new AuthorizationInterceptor(userService, googleUserService, Arrays.asList(Role.USER, Role.ADMIN)))
                .addPathPatterns("/reader", "/reader/*");
        registry.addInterceptor(
                new AuthorizationInterceptor(userService, googleUserService, Arrays.asList(Role.ADMIN)))
                .addPathPatterns("/admin/*");
        registry.addInterceptor(
                new CronJobInterceptor(googleUserService))
                .addPathPatterns("/cron/*");
        registry.addInterceptor(
                new TaskInterceptor(googleUserService))
                .addPathPatterns("/tasks/*");
    }

}
