package jreader.converter;

import jreader.domain.ArchivedEntry;
import jreader.dto.ArchivedEntryDto;

import org.springframework.core.convert.converter.Converter;

public class ArchivedEntryDtoConverter implements Converter<ArchivedEntry, ArchivedEntryDto> {

    @Override
    public ArchivedEntryDto convert(ArchivedEntry entity) {
        ArchivedEntryDto dto = new ArchivedEntryDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAuthor(entity.getAuthor());
        dto.setLink(entity.getLink());
        dto.setPublishedDate(entity.getPublishedDate());
        if (entity.getArchive() != null) {
            dto.setArchiveId(String.valueOf(entity.getArchive().getId()));
            dto.setArchiveTitle(entity.getArchive().getTitle());
        }
        return dto;
    }

}
