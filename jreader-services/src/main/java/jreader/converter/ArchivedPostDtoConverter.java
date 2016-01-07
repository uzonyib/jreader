package jreader.converter;

import jreader.domain.ArchivedPost;
import jreader.dto.ArchivedPostDto;

import org.springframework.core.convert.converter.Converter;

public class ArchivedPostDtoConverter implements Converter<ArchivedPost, ArchivedPostDto> {

    @Override
    public ArchivedPostDto convert(final ArchivedPost entity) {
        final ArchivedPostDto dto = new ArchivedPostDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setAuthor(entity.getAuthor());
        dto.setLink(entity.getLink());
        dto.setPublishDate(entity.getPublishDate());
        if (entity.getArchive() != null) {
            dto.setArchiveId(String.valueOf(entity.getArchive().getId()));
            dto.setArchiveTitle(entity.getArchive().getTitle());
        }
        return dto;
    }

}
