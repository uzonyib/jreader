package jreader.converter;

import jreader.domain.ArchivedPost;
import jreader.dto.ArchivedPostDto;

import org.springframework.core.convert.converter.Converter;

public class ArchivedPostDtoConverter implements Converter<ArchivedPost, ArchivedPostDto> {

    @Override
    public ArchivedPostDto convert(final ArchivedPost entity) {
        String archiveId = null;
        String archiveTitle = null;
        if (entity.getArchive() != null) {
            archiveId = String.valueOf(entity.getArchive().getId());
            archiveTitle = entity.getArchive().getTitle();
        }
        
        return ArchivedPostDto.builder()
                .id(String.valueOf(entity.getId()))
                .archiveId(archiveId)
                .archiveTitle(archiveTitle)
                .title(entity.getTitle())
                .description(entity.getDescription())
                .author(entity.getAuthor())
                .link(entity.getLink())
                .publishDate(entity.getPublishDate())
                .build();
    }

}
