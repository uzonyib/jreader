package jreader.converter;

import jreader.domain.ArchivedEntry;
import jreader.domain.FeedEntry;

import org.springframework.core.convert.converter.Converter;

public class ArchivedEntryConverter implements Converter<FeedEntry, ArchivedEntry> {

    @Override
    public ArchivedEntry convert(final FeedEntry entity) {
        ArchivedEntry archivedEntity = new ArchivedEntry();
        archivedEntity.setTitle(entity.getTitle());
        archivedEntity.setDescription(entity.getDescription());
        archivedEntity.setAuthor(entity.getAuthor());
        archivedEntity.setLink(entity.getLink());
        archivedEntity.setPublishedDate(entity.getPublishedDate());
        return archivedEntity;
    }

}
