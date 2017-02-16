package jreader.test.acceptance.util;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("jreader.test.acceptance")
public class CucumberTestConfiguration {

    @Autowired
    private BrowserManager browserManager;

    @Bean
    public WebDriver browser() {
        return browserManager.getBrowser();
    }

}