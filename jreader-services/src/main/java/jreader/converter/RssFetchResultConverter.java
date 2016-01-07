package jreader.converter;

import java.util.ArrayList;
import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Post;
import jreader.dto.RssFetchResult;

import org.springframework.core.convert.converter.Converter;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class RssFetchResultConverter implements Converter<SyndFeed, RssFetchResult> {

    @Override
    public RssFetchResult convert(final SyndFeed syndFeed) {
        final RssFetchResult result = new RssFetchResult();
        result.setFeed(new Feed());
        result.setPosts(new ArrayList<Post>());

        result.getFeed().setTitle(syndFeed.getTitle());
        result.getFeed().setDescription(syndFeed.getDescription());
        result.getFeed().setFeedType(syndFeed.getFeedType());
        if (syndFeed.getEntries() != null) {
            for (SyndEntry entry : (List<SyndEntry>) syndFeed.getEntries()) {
                result.getPosts().add(convert(entry));
            }
        }
        return result;
    }

    private static Post convert(final SyndEntry entry) {
        final Post post = new Post();
        post.setUri(entry.getUri());
        post.setTitle(entry.getTitle());
        if (entry.getDescription() != null) {
            post.setDescription(entry.getDescription().getValue());
        }
        post.setAuthor(entry.getAuthor());
        post.setLink(entry.getLink());
        if (entry.getPublishedDate() != null) {
            post.setPublishDate(entry.getPublishedDate().getTime());
        } else if (entry.getUpdatedDate() != null) {
            post.setPublishDate(entry.getUpdatedDate().getTime());
        }
        return post;
    }

}
