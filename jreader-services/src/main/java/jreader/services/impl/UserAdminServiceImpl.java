package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.dto.UserDto;
import jreader.services.UserAdminService;

public class UserAdminServiceImpl implements UserAdminService {
    
    private UserDao userDao;
    
    private ConversionService conversionService;
    
    public UserAdminServiceImpl(final UserDao userDao, final ConversionService conversionService) {
        this.userDao = userDao;
        this.conversionService = conversionService;
    }

    @Override
    public List<UserDto> list(final int offset, final int count) {
        final List<User> users = userDao.list(offset, count);
        final List<UserDto> dtos = new ArrayList<UserDto>();
        for (User user : users) {
            dtos.add(conversionService.convert(user, UserDto.class));
        }
        return dtos;
    }
    
    @Override
    public void updateRole(final String username, final String role) {
        final User user = userDao.find(username);
        if (user != null) {
            user.setRole(Role.valueOf(role));
            userDao.save(user);
        }
    }

}