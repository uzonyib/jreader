package jreader.converter;

import jreader.domain.Archive;
import jreader.dto.ArchiveDto;

import org.springframework.core.convert.converter.Converter;

public class ArchiveDtoConverter implements Converter<Archive, ArchiveDto> {

    @Override
    public ArchiveDto convert(final Archive entity) {
        ArchiveDto dto = new ArchiveDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setTitle(entity.getTitle());
        dto.setOrder(entity.getOrder());
        return dto;
    }

}
