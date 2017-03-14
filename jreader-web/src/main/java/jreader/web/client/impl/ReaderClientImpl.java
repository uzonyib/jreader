package jreader.web.client.impl;

import java.net.URI;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jreader.web.client.ReaderClient;
import jreader.web.controller.ResponseEntity;

public class ReaderClientImpl implements ReaderClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public ReaderClientImpl(final String baseUrl, final RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public void createGroup(final String title) {
        final URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("reader", "groups").queryParam("title", title).build().toUri();
        restTemplate.postForEntity(uri, null, ResponseEntity.class);
    }

    @Override
    public void deleteGroup(final Long groupId) {
        final URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("reader", "groups", String.valueOf(groupId)).build().toUri();
        restTemplate.delete(uri);
    }

    @Override
    public void renameGroup(final Long groupId, final String title) {
        final URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment("reader", "groups", String.valueOf(groupId), "title").queryParam("value", title).build().toUri();
        restTemplate.put(uri, null);
    }

}
