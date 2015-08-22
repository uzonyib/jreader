package jreader.web.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class FeedRegistry {
    
    private Map<String, String> feedUrls = new HashMap<String, String>();
    private Map<String, SyndFeed> feeds = new HashMap<String, SyndFeed>();
    private Map<String, List<SyndEntry>> feedEntries = new HashMap<String, List<SyndEntry>>();
    
    public SyndFeed registerFeed(String title, String url) throws Exception {
        SyndFeed feed = mock(SyndFeed.class);
        when(feed.getTitle()).thenReturn(title);
        feedUrls.put(title, url);
        feeds.put(title, feed);
        feedEntries.put(title, new ArrayList<SyndEntry>());
        return feed;
    }
    
    public void registerEntry(String feedTitle, String title, String description, String author, String link, Date publishedDate) {
        SyndEntry entry = mock(SyndEntry.class);
        when(entry.getTitle()).thenReturn(title);
        SyndContent content = mock(SyndContent.class);
        when(content.getValue()).thenReturn(description);
        when(entry.getDescription()).thenReturn(content);
        when(entry.getAuthor()).thenReturn(author);
        when(entry.getLink()).thenReturn(link);
        when(entry.getPublishedDate()).thenReturn(publishedDate);
        feedEntries.get(feedTitle).add(entry);
        when(feeds.get(feedTitle).getEntries()).thenReturn(feedEntries.get(feedTitle));
    }
    
    public String getUrl(String title) {
        return feedUrls.get(title);
    }

}
