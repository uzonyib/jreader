package jreader.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import(ServiceConfig.class)
@PropertySource({ "classpath:cron.properties", "classpath:application.properties" })
@ComponentScan({ "jreader.web.service", "jreader.web.controller" })
public class ControllerConfig extends WebMvcConfigurerAdapter {

}
