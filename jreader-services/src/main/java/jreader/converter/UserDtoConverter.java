package jreader.converter;

import org.springframework.core.convert.converter.Converter;

import jreader.domain.User;
import jreader.dto.UserDto;

public class UserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(final User entity) {
        final UserDto dto = new UserDto();
        dto.setUsername(entity.getUsername());
        dto.setRole(entity.getRole().name());
        return dto;
    }

}
