package jreader.web.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.googlecode.objectify.ObjectifyFilter;

import jreader.domain.Role;
import jreader.web.interceptor.AuthorizationInterceptor;

@Configuration
@Import(ControllerConfig.class)
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ServiceConfig serviceConfig;

    @Bean
    public FilterRegistrationBean getObjectifyFilter() {
        final FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new ObjectifyFilter());
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

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
                new AuthorizationInterceptor(serviceConfig.userService(), serviceConfig.googleUserService(), Arrays.asList(Role.USER, Role.ADMIN)))
                .addPathPatterns("/reader", "/reader/*");
        registry.addInterceptor(
                new AuthorizationInterceptor(serviceConfig.userService(), serviceConfig.googleUserService(), Arrays.asList(Role.ADMIN)))
                .addPathPatterns("/admin/*");
    }

}
