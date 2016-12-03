package jreader.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.services.DateHelper;
import jreader.services.ServiceException;

public class UserServiceImplTest {
	
	private static final String NEW_USER = "new_user";
	private static final String EXISTING_USER = "existing_user";
	private static final Long CURRENT_DATE = 123L;
	
	@InjectMocks
	private UserServiceImpl service;
	
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
		when(userDao.find(NEW_USER)).thenReturn(null);
		when(userDao.find(EXISTING_USER)).thenReturn(user);
		when(dateHelper.getCurrentDate()).thenReturn(CURRENT_DATE);
	}
	
	@Test
	public void register_IfUserIsNew_ShouldSaveUser() {
		service.register(NEW_USER, Role.UNAUTHORIZED);
		
		verify(userDao).save(userCaptor.capture());
		assertEquals(userCaptor.getValue().getUsername(), NEW_USER);
		assertEquals(userCaptor.getValue().getRole(), Role.UNAUTHORIZED);
		assertEquals(userCaptor.getValue().getRegistrationDate(), CURRENT_DATE);
	}
	
	@Test
	public void register_IfUserIsNewAdmin_ShouldSaveUser() {
		service.register(NEW_USER, Role.ADMIN);
		
		verify(userDao).save(userCaptor.capture());
		assertEquals(userCaptor.getValue().getUsername(), NEW_USER);
		assertEquals(userCaptor.getValue().getRole(), Role.ADMIN);
		assertEquals(userCaptor.getValue().getRegistrationDate(), CURRENT_DATE);
	}
	
	@Test
	public void register_IfUserExists_ShouldThrowException() {
		try {
			service.register(EXISTING_USER, Role.UNAUTHORIZED);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
		}
		
		verify(userDao, never()).save(any(User.class));
	}
	
	@Test
    public void isRegistered_IfUserIsNew_ShouldReturnFalse() {
        boolean actual = service.isRegistered(NEW_USER);
        
        assertFalse(actual);
    }
	
	@Test
    public void isRegistered_IfUserExists_ShouldReturnTrue() {
        boolean actual = service.isRegistered(EXISTING_USER);
        
        assertTrue(actual);
    }
    
    @Test
    public void getRole_IfUserIsNew_ShouldReturnNull() {
        Role actual = service.getRole(NEW_USER);
        
        assertNull(actual, null);
    }
    
    @Test
    public void getRole_IfUserExists_ShouldReturnRole() {
        Role actual = service.getRole(EXISTING_USER);
        
        assertEquals(actual, Role.USER);
    }

}
