package jreader.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;

import java.net.URL;

import jreader.domain.Feed;
import jreader.dto.RssFetchResult;
import jreader.services.FeedReader;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RssServiceImplTest {
	
	private static final String RSS_URL = "http://domain.com/rss";

	@Mock
	private FeedReader feedReader;
	
	@InjectMocks
	private RssServiceImpl service;
	
	@Mock
	private RssFetchResult rssFetchResult;
	
	@Mock
	private Feed feed;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void fetchSuccessfully() throws Exception {
		URL url = new URL(RSS_URL);
		when(feedReader.fetch(url)).thenReturn(rssFetchResult);
		when(rssFetchResult.getFeed()).thenReturn(feed);
		
		service.fetch(RSS_URL);
		verify(feedReader).fetch(url);
		verify(feed).setUrl(RSS_URL);
	}

	@Test
	public void fetchWithError() throws Exception {
		URL url = new URL(RSS_URL);
		when(feedReader.fetch(url)).thenThrow(new Exception());
		
		RssFetchResult result = service.fetch(RSS_URL);
		verify(feedReader).fetch(url);
		assertNull(result);
	}
	
}
