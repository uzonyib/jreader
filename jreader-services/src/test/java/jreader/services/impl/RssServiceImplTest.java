package jreader.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.net.URL;

import jreader.domain.Feed;
import jreader.dto.RssFetchResult;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.rometools.fetcher.FeedFetcher;
import com.rometools.rome.feed.synd.SyndFeed;

public class RssServiceImplTest {
	
	private static final String RSS_URL = "http://domain.com/rss";

	@Mock
	private FeedFetcher feedFetcher;
	@Mock
	private ConversionService conversionService;
	
	@InjectMocks
	private RssServiceImpl service;
	
	@Mock
	private RssFetchResult rssFetchResult;
	@Mock
	private Feed feed;
	@Mock
	private SyndFeed syndFeed;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void fetchSuccessfully() throws Exception {
		URL url = new URL(RSS_URL);
		when(feedFetcher.retrieveFeed(url)).thenReturn(syndFeed);
		when(conversionService.convert(syndFeed, RssFetchResult.class)).thenReturn(rssFetchResult);
		when(rssFetchResult.getFeed()).thenReturn(feed);
		
		service.fetch(RSS_URL);
		verify(feedFetcher).retrieveFeed(url);
		verify(conversionService).convert(syndFeed, RssFetchResult.class);
		verify(feed).setUrl(RSS_URL);
	}

	@Test
	public void fetchWithError() throws Exception {
		URL url = new URL(RSS_URL);
		when(feedFetcher.retrieveFeed(url)).thenThrow(new IOException());
		
		RssFetchResult result = service.fetch(RSS_URL);
		verify(feedFetcher).retrieveFeed(url);
		assertNull(result);
	}
	
}
