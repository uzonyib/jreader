package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.dao.UserDao;
import jreader.dao.impl.EntityFactory;
import jreader.domain.Archive;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.services.ServiceException;
import jreader.services.ServiceStatus;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchiveServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final String ARCHIVE_TITLE = "archive_title";
	private static final int ARCHIVE_ORDER = 10;
	private static final long ARCHIVE_ID = 123;
	
	@InjectMocks
	private ArchiveServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private ArchiveDao archiveDao;
	@Mock
	private ConversionService conversionService;
	@Mock
	private EntityFactory entityFactory;
	
	@Mock
	private User user;
	@Mock
	private Archive archive;
	@Mock
	private Archive archive1;
	@Mock
	private Archive archive2;
	
	@Mock
	private ArchiveDto archiveDto;
	@Mock
	private ArchiveDto archiveDto1;
	@Mock
	private ArchiveDto archiveDto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createNewArchive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_TITLE)).thenReturn(null);
		when(archiveDao.getMaxOrder(user)).thenReturn(ARCHIVE_ORDER - 1);
		when(entityFactory.createArchive(user, ARCHIVE_TITLE, ARCHIVE_ORDER)).thenReturn(archive);
		when(archiveDao.save(archive)).thenReturn(archive);
		when(conversionService.convert(archive, ArchiveDto.class)).thenReturn(archiveDto);
		
		ArchiveDto result = service.createArchive(USERNAME, ARCHIVE_TITLE);
		assertEquals(result, archiveDto);
		
		verify(userDao).find(USERNAME);
		verify(archiveDao).find(user, ARCHIVE_TITLE);
		verify(archiveDao).getMaxOrder(user);
		verify(archiveDao).save(archive);
	}
	
	@Test
	public void createExistingArchive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_TITLE)).thenReturn(archive);
		
		try {
			service.createArchive(USERNAME, ARCHIVE_TITLE);
			fail();
		} catch(ServiceException e) {
			assertEquals(e.getStatus(), ServiceStatus.RESOURCE_ALREADY_EXISTS);
		}
		
		verify(userDao).find(USERNAME);
		verify(archiveDao).find(user, ARCHIVE_TITLE);
		verify(archiveDao, never()).save(archive);
	}
	
	@Test
	public void deleteArchive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		
		service.deleteArchive(USERNAME, ARCHIVE_ID);
		
		verify(userDao).find(USERNAME);
		verify(archiveDao).delete(archive);
	}
	
	@Test
	public void entitleArchive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		
		service.entitle(USERNAME, ARCHIVE_ID, ARCHIVE_TITLE);
		
		verify(userDao).find(USERNAME);
		verify(archive).setTitle(ARCHIVE_TITLE);
		verify(archiveDao).save(archive);
	}
	

	@Test
	public void moveArchiveUp() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));
		final long id = 100L;
		when(archive.getId()).thenReturn(ARCHIVE_ID);
		when(archive1.getId()).thenReturn(id);
		when(archive2.getId()).thenReturn(200L);
		
		when(archive.getOrder()).thenReturn(1);
		when(archive1.getOrder()).thenReturn(2);
		when(archive2.getOrder()).thenReturn(3);
		
		service.moveUp(USERNAME, id);
		
		verify(archive).setOrder(2);
		verify(archive1).setOrder(1);
		verify(archiveDao).saveAll(Arrays.asList(archive, archive1));
		verify(archive2, never()).setOrder(anyInt());
	}
	
	@Test
	public void moveArchiveDown() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));
		final long id = 100L;
		when(archive.getId()).thenReturn(ARCHIVE_ID);
		when(archive1.getId()).thenReturn(id);
		when(archive2.getId()).thenReturn(200L);
		
		when(archive.getOrder()).thenReturn(1);
		when(archive1.getOrder()).thenReturn(2);
		when(archive2.getOrder()).thenReturn(3);
		
		service.moveDown(USERNAME, id);
		
		verify(archive1).setOrder(3);
		verify(archive2).setOrder(2);
		verify(archiveDao).saveAll(Arrays.asList(archive1, archive2));
		verify(archive, never()).setOrder(anyInt());
	}
	
	@Test
	public void list() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));
		
		when(conversionService.convert(archive, ArchiveDto.class)).thenReturn(archiveDto);
		when(conversionService.convert(archive1, ArchiveDto.class)).thenReturn(archiveDto1);
		when(conversionService.convert(archive2, ArchiveDto.class)).thenReturn(archiveDto2);
		
		List<ArchiveDto> result = service.list(USERNAME);
		Assert.assertEquals(result, Arrays.asList(archiveDto, archiveDto1, archiveDto2));
	}

}
