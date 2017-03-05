package jreader.feed.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jreader.feed.client.FeedApiClient;
import jreader.feed.client.FeedApiClientImpl;

@Configuration
public class ClientConfiguration {

    static final String BASE_URL = "http://server";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public FeedApiClient feedApiClient() {
        return new FeedApiClientImpl(BASE_URL, restTemplateBuilder.build());
    }

}
