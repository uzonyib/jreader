package jreader.converter;

import jreader.domain.Group;
import jreader.dto.GroupDto;

import org.springframework.core.convert.converter.Converter;

public class GroupDtoConverter implements Converter<Group, GroupDto> {

    @Override
    public GroupDto convert(final Group entity) {
        return new GroupDto(String.valueOf(entity.getId()), entity.getTitle(), entity.getOrder());
    }

}
