package jreader.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jreader.domain.User;
import jreader.dto.UserDto;

@Component
public class UserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(final User entity) {
        return new UserDto(entity.getUsername(), entity.getRole().name(), entity.getRegistrationDate());
    }

}
