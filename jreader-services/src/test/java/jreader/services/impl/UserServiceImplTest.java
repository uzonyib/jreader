package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.services.DateHelper;
import jreader.services.exception.ResourceAlreadyExistsException;

public class UserServiceImplTest extends ServiceTest {

    private static final String NEW_USER = "new_user";
    private static final String EXISTING_USER = "existing_user";
    private static final Long CURRENT_DATE = 123L;

    @InjectMocks
    private UserServiceImpl sut;

    @Mock
    private UserDao userDao;

    @Mock
    private DateHelper dateHelper;

    @Mock
    private User user;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(user.getRole()).thenReturn(Role.USER);
        when(userDao.find(NEW_USER)).thenReturn(Optional.empty());
        when(userDao.find(EXISTING_USER)).thenReturn(Optional.of(user));
        when(dateHelper.getCurrentDate()).thenReturn(CURRENT_DATE);
    }

    @Test
    public void register_ShouldSaveUser_IfUserIsNew() {
        sut.register(NEW_USER, Role.UNAUTHORIZED);

        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo(NEW_USER);
        assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.UNAUTHORIZED);
        assertThat(userCaptor.getValue().getRegistrationDate()).isEqualTo(CURRENT_DATE);
    }

    @Test
    public void register_ShouldSaveUser_IfUserIsNewAdmin() {
        sut.register(NEW_USER, Role.ADMIN);

        verify(userDao).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo(NEW_USER);
        assertThat(userCaptor.getValue().getRole()).isEqualTo(Role.ADMIN);
        assertThat(userCaptor.getValue().getRegistrationDate()).isEqualTo(CURRENT_DATE);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidUsernames")
    public void register_ShouldThrowException_IfUsernameIsInvalid(String username) {
        sut.register(username, Role.UNAUTHORIZED);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void register_ShouldThrowException_IfRoleIsNull() {
        final Role role = null;

        sut.register(NEW_USER, role);
    }

    @Test(expectedExceptions = ResourceAlreadyExistsException.class)
    public void register_ShouldThrowException_IfUserAlreadyExists() {
        try {
            sut.register(EXISTING_USER, Role.UNAUTHORIZED);
        } finally {
            verify(userDao, never()).save(any(User.class));
        }
    }

    @Test
    public void isRegistered_ShouldReturnFalse_IfUserIsNew() {
        final boolean actual = sut.isRegistered(NEW_USER);

        assertThat(actual).isFalse();
    }

    @Test
    public void isRegistered_ShouldReturnTrue_IfUserExists() {
        final boolean actual = sut.isRegistered(EXISTING_USER);

        assertThat(actual).isTrue();
    }

    @Test
    public void getRole_ShouldReturnNull_IfUserIsNew() {
        Role actual = sut.getRole(NEW_USER);

        assertThat(actual).isNull();
    }

    @Test
    public void getRole_ShouldReturnRole_IfUserExists() {
        final Role actual = sut.getRole(EXISTING_USER);

        assertThat(actual).isEqualTo(Role.USER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidUsernames")
    public void getRole_ShouldThrowException_IfUsernameIsInvalid(String username) {
        sut.getRole(username);
    }

}
