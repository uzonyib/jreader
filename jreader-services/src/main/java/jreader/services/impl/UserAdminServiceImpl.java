package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.dto.UserDto;

public class UserAdminServiceImpl implements UserAdminService {
    
    private UserDao userDao;
    
    private ConversionService conversionService;
    
    public UserAdminServiceImpl(final UserDao userDao, final ConversionService conversionService) {
        this.userDao = userDao;
        this.conversionService = conversionService;
    }

    @Override
    public List<UserDto> list(final int offset, final int count) {
        List<User> users = userDao.list(offset, count);
        List<UserDto> dtos = new ArrayList<UserDto>();
        for (User user : users) {
            dtos.add(conversionService.convert(user, UserDto.class));
        }
        return dtos;
    }

}
