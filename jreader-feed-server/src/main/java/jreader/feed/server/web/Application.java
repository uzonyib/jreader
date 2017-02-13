package jreader.feed.server.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "jreader.feed.server")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
