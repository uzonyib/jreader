package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
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
import jreader.services.exception.FetchException;

public class RssServiceImplTest {

    private static final String RSS_URL = "http://domain.com/rss";

    @InjectMocks
    private RssServiceImpl sut;

    @Mock
    private FeedFetcher feedFetcher;
    @Mock
    private ConversionService conversionService;

    private URL url;

    @Mock
    private Feed feed;
    @Mock
    private SyndFeed syndFeed;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        MockitoAnnotations.initMocks(this);

        url = new URL(RSS_URL);
    }

    @Test
    public void fetch_ShouldBeSuccessful_IfUrlIsValid() throws Exception {
        final RssFetchResult rssFetchResult = new RssFetchResult(feed, null);
        when(feedFetcher.retrieveFeed(url)).thenReturn(syndFeed);
        when(conversionService.convert(syndFeed, RssFetchResult.class)).thenReturn(rssFetchResult);

        final RssFetchResult actual = sut.fetch(RSS_URL);

        verify(feedFetcher).retrieveFeed(url);
        verify(conversionService).convert(syndFeed, RssFetchResult.class);
        verify(feed).setUrl(RSS_URL);
        assertThat(actual).isNotNull();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void fetch_ShouldThrowException_IfUrlIsInvalid() {
        final String invalidUrl = "domain.com/rss";

        sut.fetch(invalidUrl);
    }

    @Test(expectedExceptions = FetchException.class)
    public void fetch_ShouldThrowException_IfFetchFails() throws Exception {
        when(feedFetcher.retrieveFeed(url)).thenThrow(new FetchException());

        try {
            sut.fetch(RSS_URL);
        } finally {
            verify(feedFetcher).retrieveFeed(url);
        }
    }

}
