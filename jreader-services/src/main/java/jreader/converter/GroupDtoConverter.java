package jreader.converter;

import jreader.domain.Group;
import jreader.dto.GroupDto;

import org.springframework.core.convert.converter.Converter;

public class GroupDtoConverter implements Converter<Group, GroupDto> {

    @Override
    public GroupDto convert(final Group entity) {
        final GroupDto dto = new GroupDto();
        dto.setId(String.valueOf(entity.getId()));
        dto.setTitle(entity.getTitle());
        dto.setOrder(entity.getOrder());
        return dto;
    }

}
