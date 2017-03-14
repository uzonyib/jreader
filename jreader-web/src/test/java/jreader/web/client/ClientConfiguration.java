package jreader.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jreader.web.client.impl.ReaderClientImpl;

@Configuration
public class ClientConfiguration {
    
    static final String BASE_URL = "http://server";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    public ReaderClient readerClient() {
        return new ReaderClientImpl(BASE_URL, restTemplateBuilder.build());
    }

}
