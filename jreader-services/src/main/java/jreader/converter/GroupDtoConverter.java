package jreader.converter;

import jreader.domain.Group;
import jreader.dto.GroupDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GroupDtoConverter implements Converter<Group, GroupDto> {

    @Override
    public GroupDto convert(final Group entity) {
        return GroupDto.builder().id(entity.getId()).title(entity.getTitle()).order(entity.getOrder()).build();
    }

}
