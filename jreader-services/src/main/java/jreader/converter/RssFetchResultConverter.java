package jreader.converter;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import jreader.domain.Feed;
import jreader.domain.Post;
import jreader.dto.RssFetchResult;

@Component
public class RssFetchResultConverter implements Converter<SyndFeed, RssFetchResult> {

    @Override
    public RssFetchResult convert(final SyndFeed syndFeed) {
        final RssFetchResult result = new RssFetchResult(new Feed(), new ArrayList<Post>());

        result.getFeed().setTitle(syndFeed.getTitle());
        result.getFeed().setDescription(syndFeed.getDescription());
        result.getFeed().setFeedType(syndFeed.getFeedType());
        if (syndFeed.getEntries() != null) {
            syndFeed.getEntries().forEach(entry -> result.getPosts().add(convert(entry)));
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
