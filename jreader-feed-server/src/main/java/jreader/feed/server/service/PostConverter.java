package jreader.feed.server.service;

import java.util.Calendar;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;

import jreader.feed.server.model.Post;

@Service
public class PostConverter implements Converter<Post, SyndEntry> {

    @Override
    public SyndEntry convert(final Post post) {
        final SyndEntryImpl syndEntry = new SyndEntryImpl();
        syndEntry.setTitle(post.getTitle());
        final SyndContentImpl description = new SyndContentImpl();
        description.setValue(post.getDescription());
        syndEntry.setDescription(description);
        syndEntry.setAuthor(post.getAuthor());
        final Calendar publishDate = Calendar.getInstance();
        publishDate.setTimeInMillis(post.getPublishDate());
        syndEntry.setPublishedDate(publishDate.getTime());
        return syndEntry;
    }

}
