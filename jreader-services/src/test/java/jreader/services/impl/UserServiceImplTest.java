package jreader.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.services.ServiceException;
import jreader.services.ServiceStatus;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
		
		service.register(NEW_USER);
		
		verify(userDao).save(userCaptor.capture());
		assertEquals(userCaptor.getValue().getUsername(), NEW_USER);
	}
	
	@Test
	public void registerExistingUser() {
		when(userDao.find(EXISTING_USER)).thenReturn(user);

		try {
			service.register(EXISTING_USER);
			fail();
		} catch (ServiceException e) {
			assertEquals(e.getStatus(), ServiceStatus.RESOURCE_ALREADY_EXISTS);
		}
		
		verify(userDao, never()).save(any(User.class));
	}

}
