package jreader.converter;

import jreader.domain.ArchivedPost;
import jreader.domain.Post;

import org.springframework.core.convert.converter.Converter;

public class ArchivedPostConverter implements Converter<Post, ArchivedPost> {

    @Override
    public ArchivedPost convert(final Post entity) {
        final ArchivedPost archivedPost = new ArchivedPost();
        archivedPost.setTitle(entity.getTitle());
        archivedPost.setDescription(entity.getDescription());
        archivedPost.setAuthor(entity.getAuthor());
        archivedPost.setLink(entity.getLink());
        archivedPost.setPublishDate(entity.getPublishDate());
        return archivedPost;
    }

}
