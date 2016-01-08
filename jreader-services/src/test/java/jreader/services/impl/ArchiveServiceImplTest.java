package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.BuilderFactory;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;
import jreader.services.ArchivedPostFilter;
import jreader.services.ServiceException;

public class ArchiveServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final Long GROUP_ID = 1L;
	private static final Long SUBSCRIPTION_ID = 11L;
	private static final Long POST_ID = 21L;
	private static final String ARCHIVE_TITLE = "archive_title";
	private static final int ARCHIVE_ORDER = 10;
	private static final long ARCHIVE_ID = 123;
	private static final long ARCHIVED_POST_ID = 456;
	
	@InjectMocks
	private ArchiveServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private GroupDao groupDao;
	@Mock
	private SubscriptionDao subscriptionDao;
	@Mock
	private PostDao postDao;
	@Mock
	private ArchiveDao archiveDao;
	@Mock
	private ArchivedPostDao archivedPostDao;
	@Mock
	private ConversionService conversionService;
	@Mock
	private BuilderFactory builderFactory;
	
	@Mock
	private User user;
	@Mock
	private Group group;
	@Mock
	private Subscription subscription;
	@Mock
	private Post post;
	@Mock
	private Archive archive;
	@Mock
	private Archive archive1;
	@Mock
	private Archive archive2;
	@Mock
	private ArchivedPost archivedPost;
	@Mock
	private ArchivedPost archivedPost1;
	@Mock
	private ArchivedPost archivedPost2;
	@Mock
	private Archive.Builder archiveBuilder;
	
	@Mock
	private ArchiveDto archiveDto;
	@Mock
	private ArchiveDto archiveDto1;
	@Mock
	private ArchiveDto archiveDto2;
	@Mock
	private ArchivedPostDto archivedPostDto1;
	@Mock
	private ArchivedPostDto archivedPostDto2;
	@Mock
	private ArchivedPostFilter filter;
	
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
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
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
		when(groupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.find(group, SUBSCRIPTION_ID)).thenReturn(subscription);
		when(postDao.find(subscription, POST_ID)).thenReturn(post);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(conversionService.convert(post, ArchivedPost.class)).thenReturn(archivedPost);
		
		service.archive(USERNAME, GROUP_ID, SUBSCRIPTION_ID, POST_ID, ARCHIVE_ID);
		
		verify(conversionService).convert(post, ArchivedPost.class);
		verify(archivedPost).setArchive(archive);
		verify(archivedPostDao).save(archivedPost);
	}
	
	@Test
	public void listPostsForUser() {
		when(filter.getArchiveId()).thenReturn(null);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archivedPostDao.list(user, filter)).thenReturn(Arrays.asList(archivedPost1, archivedPost2));
		when(conversionService.convert(archivedPost1, ArchivedPostDto.class)).thenReturn(archivedPostDto1);
		when(conversionService.convert(archivedPost2, ArchivedPostDto.class)).thenReturn(archivedPostDto2);
		
		List<ArchivedPostDto> result = service.listPosts(filter);
		
		verify(archivedPostDao).list(user, filter);
		Assert.assertEquals(result, Arrays.asList(archivedPostDto1, archivedPostDto2));
	}
	
	@Test
	public void listPostsForArchive() {
		when(filter.getArchiveId()).thenReturn(ARCHIVE_ID);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(archivedPostDao.list(archive, filter)).thenReturn(Arrays.asList(archivedPost1, archivedPost2));
		when(conversionService.convert(archivedPost1, ArchivedPostDto.class)).thenReturn(archivedPostDto1);
		when(conversionService.convert(archivedPost2, ArchivedPostDto.class)).thenReturn(archivedPostDto2);
		
		List<ArchivedPostDto> result = service.listPosts(filter);
		
		verify(archivedPostDao).list(archive, filter);
		Assert.assertEquals(result, Arrays.asList(archivedPostDto1, archivedPostDto2));
	}
	
	@Test
	public void unsubscribe() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(archiveDao.find(user, ARCHIVE_ID)).thenReturn(archive);
		when(archivedPostDao.find(archive, ARCHIVED_POST_ID)).thenReturn(archivedPost);
		
		service.deletePost(USERNAME, ARCHIVE_ID, ARCHIVED_POST_ID);
		
		verify(archivedPostDao).delete(archivedPost);
	}

}
