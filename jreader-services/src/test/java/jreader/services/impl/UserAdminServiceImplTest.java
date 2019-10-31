package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.dto.UserDto;
import jreader.services.exception.ResourceNotFoundException;

public class UserAdminServiceImplTest {

    private static final String USERNAME = "user1";

    @InjectMocks
    private UserAdminServiceImpl sut;

    @Mock
    private UserDao userDao;

    @Mock
    private User user1;
    @Mock
    private User user2;

    private UserDto dto1;
    private UserDto dto2;

    @Mock
    private ConversionService conversionService;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void list_ShouldReturnUsers() {
        final List<User> entities = Arrays.asList(user1, user2);
        when(userDao.list(anyInt(), anyInt())).thenReturn(entities);

        dto1 = new UserDto(USERNAME, Role.ADMIN.name(), 1000L);
        dto2 = new UserDto("user2", Role.USER.name(), 2000L);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(User.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserDto.class)))).thenReturn(Arrays.asList(dto1, dto2));

        final List<UserDto> actual = sut.list(0, 10);

        assertThat(actual).hasSize(2);
        assertThat(actual).isEqualTo(Arrays.asList(dto1, dto2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidListParameters")
    public void list_ShouldNotAllowNegativeOffset(int offset, int count) {
        sut.list(offset, count);
    }

    @DataProvider(name = "invalidListParameters")
    private Integer[][] getInvalidListParameters() {
        return new Integer[][] {
            { -1, 10 },
            { 0, -1 },
            { 0, 0 }
        };
    }

    @Test
    public void updateRole_ShouldUpdateRole() {
        when(userDao.find(USERNAME)).thenReturn(Optional.of(user1));

        sut.updateRole(USERNAME, "ADMIN");

        verify(user1).setRole(Role.ADMIN);
        verify(userDao).save(user1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidUsernames")
    public void updateRole_NotAllowInvalidUsername(String username) {
        sut.updateRole(username, "ADMIN");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidRoles")
    public void updateRole_NotAllowInvalidRole(String role) {
        sut.updateRole(USERNAME, role);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void updateRole_ShouldThrowException_IfUserNotFound() {
        when(userDao.find(USERNAME)).thenReturn(Optional.empty());

        sut.updateRole(USERNAME, "ADMIN");
    }

}
