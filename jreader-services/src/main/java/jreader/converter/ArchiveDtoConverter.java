package jreader.converter;

import jreader.domain.Archive;
import jreader.dto.ArchiveDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArchiveDtoConverter implements Converter<Archive, ArchiveDto> {

    @Override
    public ArchiveDto convert(final Archive entity) {
        return new ArchiveDto(String.valueOf(entity.getId()), entity.getTitle(), entity.getOrder());
    }

}
