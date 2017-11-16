package jreader.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.dto.UserDto;
import jreader.services.UserAdminService;
import jreader.services.exception.ResourceNotFoundException;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private UserDao userDao;

    private ConversionService conversionService;

    @Autowired
    public UserAdminServiceImpl(final UserDao userDao, final ConversionService conversionService) {
        this.userDao = userDao;
        this.conversionService = conversionService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserDto> list(final int offset, final int count) {
        Assert.isTrue(offset >= 0, "Invalid offset");
        Assert.isTrue(count > 0, "Invalid count");

        return (List<UserDto>) conversionService.convert(userDao.list(offset, count),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(User.class)), 
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserDto.class)));
    }

    @Override
    public void updateRole(final String username, final String role) {
        Assert.hasLength(username, "Invalid username.");
        Assert.notNull(role, "Invalid role.");

        final User user = userDao.find(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found, username: " + username);
        }

        user.setRole(Role.valueOf(role));
        userDao.save(user);
    }

}
