package jreader.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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
import jreader.services.ServiceException;

public class UserServiceImplTest {
	
	private static final String NEW_USER = "new_user";
	private static final String EXISTING_USER = "existing_user";
	
	@InjectMocks
	private UserServiceImpl service;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private User user;
	
	@Captor
	private ArgumentCaptor<User> userCaptor;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void registerNewUser() {
		when(userDao.find(NEW_USER)).thenReturn(null);
		
		service.register(NEW_USER, Role.UNAUTHORIZED);
		
		verify(userDao).save(userCaptor.capture());
		assertEquals(userCaptor.getValue().getUsername(), NEW_USER);
		assertEquals(userCaptor.getValue().getRole(), Role.UNAUTHORIZED);
	}
	
	@Test
	public void registerNewAdmin() {
		when(userDao.find(NEW_USER)).thenReturn(null);
		
		service.register(NEW_USER, Role.ADMIN);
		
		verify(userDao).save(userCaptor.capture());
		assertEquals(userCaptor.getValue().getUsername(), NEW_USER);
		assertEquals(userCaptor.getValue().getRole(), Role.ADMIN);
	}
	
	@Test
	public void registerExistingUser() {
		when(userDao.find(EXISTING_USER)).thenReturn(user);

		try {
			service.register(EXISTING_USER, Role.UNAUTHORIZED);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
		}
		
		verify(userDao, never()).save(any(User.class));
	}
	
	@Test
    public void isRegisteredNewUser() {
        when(userDao.find(NEW_USER)).thenReturn(null);
        
        boolean registered = service.isRegistered(NEW_USER);
        
        assertFalse(registered);
    }
	
	@Test
    public void isRegisteredExistingUser() {
        when(userDao.find(EXISTING_USER)).thenReturn(user);
        
        boolean registered = service.isRegistered(EXISTING_USER);
        
        assertTrue(registered);
    }
    
    @Test
    public void roleIsUnauthorizedForUnregisteredUsers() {
        when(userDao.find(NEW_USER)).thenReturn(null);
        
        Role role = service.getRole(NEW_USER);
        
        assertEquals(role, null);
    }
    
    @Test
    public void roleReturnedForUsersWithRole() {
        when(userDao.find(EXISTING_USER)).thenReturn(user);
        when(user.getRole()).thenReturn(Role.USER);
        
        Role role = service.getRole(EXISTING_USER);
        
        assertEquals(role, Role.USER);
    }

}
