package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.rometools.rome.feed.synd.SyndFeed;

import jreader.domain.Feed;
import jreader.dto.RssFetchResult;
import jreader.services.FeedFetcher;

public class RssServiceImplTest {

    private static final String RSS_URL = "http://domain.com/rss";

    @InjectMocks
    private RssServiceImpl sut;

    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private ConversionService conversionService;

    @Mock
    private Feed feed;
    @Mock
    private SyndFeed syndFeed;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetch_ShouldBeSuccessful_IfUrlIsValid() throws Exception {
        final RssFetchResult rssFetchResult = new RssFetchResult(feed, null);
        final URL url = new URL(RSS_URL);
        when(feedFetcher.retrieveFeed(url)).thenReturn(syndFeed);
        when(conversionService.convert(syndFeed, RssFetchResult.class)).thenReturn(rssFetchResult);

        final RssFetchResult actual = sut.fetch(RSS_URL);

        verify(feedFetcher).retrieveFeed(url);
        verify(conversionService).convert(syndFeed, RssFetchResult.class);
        verify(feed).setUrl(RSS_URL);
        assertThat(actual).isNotNull();
    }

    @Test
    public void fetch_ShouldReturnNull_IfUrlIsInvalid() throws Exception {
        final URL url = new URL(RSS_URL);
        when(feedFetcher.retrieveFeed(url)).thenThrow(new IOException());

        final RssFetchResult actual = sut.fetch(RSS_URL);

        verify(feedFetcher).retrieveFeed(url);
        assertThat(actual).isNull();
    }

}
