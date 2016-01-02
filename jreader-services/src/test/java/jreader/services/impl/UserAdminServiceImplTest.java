package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.dto.UserDto;

public class UserAdminServiceImplTest {
	
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
        when(userDao.list(anyInt(), anyInt())).thenReturn(Arrays.asList(user1, user2));
        when(conversionService.convert(user1, UserDto.class)).thenReturn(dto1);
        when(conversionService.convert(user2, UserDto.class)).thenReturn(dto2);
        
        List<UserDto> dtos = service.list(0, 10);
        
        assertNotNull(dtos);
        assertEquals(dtos.size(), 2);
        assertSame(dtos.get(0), dto1);
        assertSame(dtos.get(1), dto2);
    }

}
