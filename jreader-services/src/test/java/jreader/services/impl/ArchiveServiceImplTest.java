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
import jreader.dao.ArchivedEntryDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.BuilderFactory;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedEntryDto;
import jreader.services.ArchivedEntryFilterData;
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
	private static final Long GROUP_ID = 1L;
	private static final Long SUBSCRIPTION_ID = 11L;
	private static final Long ENTRY_ID = 21L;
	private static final String ARCHIVE_TITLE = "archive_title";
	private static final int ARCHIVE_ORDER = 10;
	private static final long ARCHIVE_ID = 123;
	private static final long ARCHIVED_ENTRY_ID = 456;
	
	@InjectMocks
	private ArchiveServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private SubscriptionGroupDao subscriptionGroupDao;
	@Mock
	private SubscriptionDao subscriptionDao;
	@Mock
	private FeedEntryDao feedEntryDao;
	@Mock
	private ArchiveDao archiveDao;
	@Mock
	private ArchivedEntryDao archivedEntryDao;
	@Mock
	private ConversionService conversionService;
	@Mock
	private BuilderFactory builderFactory;
	
	@Mock
	private User user;
	@Mock
	private SubscriptionGroup group;
	@Mock
	private Subscription subscription;
	@Mock
	private FeedEntry entry;
	@Mock
	private Archive archive;
	@Mock
	private Archive archive1;
	@Mock
	private Archive archive2;
	@Mock
	private ArchivedEntry archivedEntry;
	@Mock
	private ArchivedEntry archivedEntry1;
	@Mock
	private ArchivedEntry archivedEntry2;
	@Mock
	private Archive.Builder archiveBuilder;
	
	@Mock
	private ArchiveDto archiveDto;
	@Mock
	private ArchiveDto archiveDto1;
	@Mock
	private ArchiveDto archiveDto2;
	@Mock
	private ArchivedEntryDto archivedEntryDto1;
	@Mock
	private ArchivedEntryDto archivedEntryDto2;
	@Mock
	private ArchivedEntryFilterData filter;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createNewArchive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_TITLE)).thenReturn(null);
		when(archiveDao.getMaxOrder(user)).thenReturn(ARCHIVE_ORDER - 1);
		when(builderFactory.createArchiveBuilder()).thenReturn(archiveBuilder);
		when(archiveBuilder.user(user)).thenReturn(archiveBuilder);
		when(archiveBuilder.title(ARCHIVE_TITLE)).thenReturn(archiveBuilder);
		when(archiveBuilder.order(ARCHIVE_ORDER)).thenReturn(archiveBuilder);
		when(archiveBuilder.build()).thenReturn(archive);
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
	
	@Test
	public void archive() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.find(group, SUBSCRIPTION_ID)).thenReturn(subscription);
		when(feedEntryDao.find(subscription, ENTRY_ID)).thenReturn(entry);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(conversionService.convert(entry, ArchivedEntry.class)).thenReturn(archivedEntry);
		
		service.archive(USERNAME, GROUP_ID, SUBSCRIPTION_ID, ENTRY_ID, ARCHIVE_ID);
		
		verify(conversionService).convert(entry, ArchivedEntry.class);
		verify(archivedEntry).setArchive(archive);
		verify(archivedEntryDao).save(archivedEntry);
	}
	
	@Test
	public void listEntriesForUser() {
		when(filter.getArchiveId()).thenReturn(null);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archivedEntryDao.list(user, filter)).thenReturn(Arrays.asList(archivedEntry1, archivedEntry2));
		when(conversionService.convert(archivedEntry1, ArchivedEntryDto.class)).thenReturn(archivedEntryDto1);
		when(conversionService.convert(archivedEntry2, ArchivedEntryDto.class)).thenReturn(archivedEntryDto2);
		
		List<ArchivedEntryDto> result = service.listEntries(filter);
		
		verify(archivedEntryDao).list(user, filter);
		Assert.assertEquals(result, Arrays.asList(archivedEntryDto1, archivedEntryDto2));
	}
	
	@Test
	public void listEntriesForArchive() {
		when(filter.getArchiveId()).thenReturn(ARCHIVE_ID);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(archivedEntryDao.list(archive, filter)).thenReturn(Arrays.asList(archivedEntry1, archivedEntry2));
		when(conversionService.convert(archivedEntry1, ArchivedEntryDto.class)).thenReturn(archivedEntryDto1);
		when(conversionService.convert(archivedEntry2, ArchivedEntryDto.class)).thenReturn(archivedEntryDto2);
		
		List<ArchivedEntryDto> result = service.listEntries(filter);
		
		verify(archivedEntryDao).list(archive, filter);
		Assert.assertEquals(result, Arrays.asList(archivedEntryDto1, archivedEntryDto2));
	}
	
	@Test
	public void unsubscribe() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(archivedEntryDao.find(archive, ARCHIVED_ENTRY_ID)).thenReturn(archivedEntry);
		
		service.deleteEntry(USERNAME, ARCHIVE_ID, ARCHIVED_ENTRY_ID);
		
		verify(archivedEntryDao).delete(archivedEntry);
	}

}
