package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.dto.UserDto;

public class UserAdminServiceImplTest {
	
	private static final String USERNAME = "user";

    @InjectMocks
	private UserAdminServiceImpl service;
	
	@Mock
	private UserDao userDao;
	
	@Mock
	private User user1;
	@Mock
    private User user2;
	
	@Mock
    private UserDto dto1;
	@Mock
    private UserDto dto2;
	
	@Mock
    private ConversionService conversionService;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    
    @Test
    public void list() {
        List<User> entities = Arrays.asList(user1, user2);
        when(userDao.list(anyInt(), anyInt())).thenReturn(entities);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(User.class)), 
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(UserDto.class)))).thenReturn(Arrays.asList(dto1, dto2));
        
        List<UserDto> dtos = service.list(0, 10);
        
        assertNotNull(dtos);
        assertEquals(dtos.size(), 2);
        assertSame(dtos.get(0), dto1);
        assertSame(dtos.get(1), dto2);
    }
    
    @Test
    public void updateRole() {
        when(userDao.find(USERNAME)).thenReturn(user1);
        
        service.updateRole(USERNAME, "ADMIN");
        
        verify(user1).setRole(Role.ADMIN);
        verify(userDao).save(user1);
    }

}
