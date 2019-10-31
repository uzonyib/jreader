package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.objectify.Ref;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.DaoFacade;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;
import jreader.services.ArchivedPostFilter;
import jreader.services.exception.ResourceAlreadyExistsException;
import jreader.services.exception.ResourceNotFoundException;

public class ArchiveServiceImplTest extends ServiceTest {

    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;
    private static final Direction DIRECTION = Direction.ASC;
    private static final String SORT_PROPERTY = "publishDate";
    private static final Pageable PAGE = new PageRequest(PAGE_NUMBER, PAGE_SIZE, new Sort(DIRECTION, SORT_PROPERTY));

    private ArchiveServiceImpl sut;

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
    private Ref<User> userRef;
    @Mock
    private Ref<Archive> archiveRef;

    private User user;
    private Group group;
    private Archive archive;
    private Archive archive1;
    private Archive archive2;
    private Subscription subscription;
    private Post post;
    private ArchivedPost archivedPost;

    @Mock
    private ArchivedPost archivedPost1;
    @Mock
    private ArchivedPost archivedPost2;

    private ArchiveDto archiveDto;
    private ArchiveDto archiveDto1;
    private ArchiveDto archiveDto2;

    private ArchivedPostDto archivedPostDto1;
    private ArchivedPostDto archivedPostDto2;

    @Captor
    private ArgumentCaptor<Archive> archiveCaptor;
    @Captor
    private ArgumentCaptor<List<Archive>> archiveListCaptor;
    @Captor
    private ArgumentCaptor<ArchivedPost> archivedPostCaptor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);
        group = Group.builder().user(user).id(0L).title("group 0").order(1).build();
        subscription = Subscription.builder().group(group).id(0L).title("subscription 0").build();
        post = new Post();
        post.setId(0L);

        archivedPost = new ArchivedPost();
        archivedPost.setId(0L);

        archive = new Archive.Builder().user(user).id(0L).title("archive 0").order(1).build();
        archive1 = new Archive.Builder().user(user).id(1L).title("archive 1").order(2).build();
        archive2 = new Archive.Builder().user(user).id(2L).title("archive 2").order(3).build();

        archiveDto = new ArchiveDto("0", "arvhive1", 1);
        archiveDto = new ArchiveDto("1", "arvhive2", 2);
        archiveDto = new ArchiveDto("2", "arvhive3", 3);

        archivedPostDto1 = ArchivedPostDto.builder().id("0").build();
        archivedPostDto2 = ArchivedPostDto.builder().id("1").build();

        final DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).postDao(postDao)
                .archiveDao(archiveDao).archivedPostDao(archivedPostDao).build();
        sut = new ArchiveServiceImpl(daoFacade, conversionService);

        when(userDao.find(user.getUsername())).thenReturn(Optional.of(user));
        when(archiveDao.find(user, archive.getId())).thenReturn(Optional.of(archive));
    }

    @Test
    public void createArchive_ShouldCreateNewArchive_IfArchiveDoesNotExist() {
        when(Ref.create(any(User.class))).thenReturn(userRef);
        when(userRef.get()).thenReturn(user);
        final String archiveTitle = "new archive";
        final int maxOrder = 10;
        when(archiveDao.find(user, archiveTitle)).thenReturn(Optional.empty());
        when(archiveDao.getMaxOrder(user)).thenReturn(maxOrder);
        when(archiveDao.save(any(Archive.class))).thenReturn(archive);
        when(conversionService.convert(archive, ArchiveDto.class)).thenReturn(archiveDto);

        final ArchiveDto actual = sut.createArchive(user.getUsername(), archiveTitle);

        assertThat(actual).isEqualTo(archiveDto);

        verify(userDao).find(user.getUsername());
        verify(archiveDao).find(user, archiveTitle);
        verify(archiveDao).getMaxOrder(user);
        verify(archiveDao).save(archiveCaptor.capture());

        assertThat(archiveCaptor.getValue().getTitle()).isEqualTo(archiveTitle);
        assertThat(archiveCaptor.getValue().getOrder()).isEqualTo(maxOrder + 1);
        assertThat(archiveCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidArchiveTitles")
    public void createArchive_ShouldThrowException_IfUsernameIsInvalid(String username) {
        sut.createArchive(username, archive.getTitle());
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidArchiveTitles")
    public void createArchive_ShouldThrowException_IfTitleIsInvalid(String title) {
        sut.createArchive(user.getUsername(), title);
    }

    @Test(expectedExceptions = ResourceAlreadyExistsException.class)
    public void createArchive_ShouldThrowException_IfArchiveAlreadyExists() {
        when(archiveDao.find(user, archive.getTitle())).thenReturn(Optional.of(archive));

        try {
            sut.createArchive(user.getUsername(), archive.getTitle());
            fail();
        } finally {
            verify(userDao).find(user.getUsername());
            verify(archiveDao).find(user, archive.getTitle());
            verifyNoMoreInteractions(archiveDao);
        }
    }

    @Test
    public void deleteArchive_ShouldDeleteArchive_IfArchiveExists() {
        final List<ArchivedPost> archivedPosts = Arrays.asList(archivedPost1, archivedPost2);
        when(archivedPostDao.list(archive)).thenReturn(archivedPosts);

        sut.deleteArchive(user.getUsername(), archive.getId());

        verify(userDao).find(user.getUsername());
        verify(archivedPostDao).deleteAll(archivedPosts);
        verify(archiveDao).delete(archive);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void deleteArchive_ShouldThrowException_IfArchiveNotFound() {
        when(archiveDao.find(user, archive.getId())).thenReturn(Optional.empty());

        sut.deleteArchive(user.getUsername(), archive.getId());
    }

    @Test
    public void entitle_ShouldUpdateArchiveTitle_IfArchiveExists() {
        final String newTitle = "new title";
        when(archiveDao.find(user, newTitle)).thenReturn(Optional.empty());

        sut.entitle(user.getUsername(), archive.getId(), newTitle);

        verify(userDao).find(user.getUsername());
        verify(archiveDao).save(archiveCaptor.capture());
        assertThat(archiveCaptor.getValue().getTitle()).isEqualTo(newTitle);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidArchiveTitles")
    public void entitle_ShouldThrowException_IfTitleIsInvalid(String title) {
        sut.entitle(user.getUsername(), archive.getId(), title);
    }

    @Test(expectedExceptions = ResourceAlreadyExistsException.class)
    public void entitle_ShouldThrowException_IfArchiveAlreadyExists() {
        final String newTitle = "new title";
        when(archiveDao.find(user, newTitle)).thenReturn(Optional.of(archive));

        sut.entitle(user.getUsername(), archive.getId(), newTitle);
    }

    @Test
    public void moveUp_ShouldUpdateArchiveOrders() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveUp(user.getUsername(), archive1.getId());

        verify(archiveDao).saveAll(archiveListCaptor.capture());
        assertThat(archiveListCaptor.getValue().get(0).getId()).isEqualTo(archive.getId());
        assertThat(archiveListCaptor.getValue().get(0).getOrder()).isEqualTo(2);
        assertThat(archiveListCaptor.getValue().get(1).getId()).isEqualTo(archive1.getId());
        assertThat(archiveListCaptor.getValue().get(1).getOrder()).isEqualTo(1);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void moveUp_ShouldThrowException_WhenArchiveNotFound() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveUp(user.getUsername(), archive2.getId() + 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void moveUp_ShouldThrowException_WhenMovingFirstArchive() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveUp(user.getUsername(), archive.getId());
    }

    @Test
    public void moveDown_ShouldUpdateArchiveOrders() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveDown(user.getUsername(), archive1.getId());

        verify(archiveDao).saveAll(archiveListCaptor.capture());
        assertThat(archiveListCaptor.getValue().get(0).getId()).isEqualTo(archive1.getId());
        assertThat(archiveListCaptor.getValue().get(0).getOrder()).isEqualTo(3);
        assertThat(archiveListCaptor.getValue().get(1).getId()).isEqualTo(archive2.getId());
        assertThat(archiveListCaptor.getValue().get(1).getOrder()).isEqualTo(2);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void moveDown_ShouldThrowException_WhenArchiveNotFound() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveDown(user.getUsername(), archive2.getId() + 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void moveDown_ShouldThrowException_WhenMovingLastArchive() {
        when(archiveDao.list(user)).thenReturn(Arrays.asList(archive, archive1, archive2));

        sut.moveDown(user.getUsername(), archive2.getId());
    }

    @Test
    public void list_ShouldReturnArchives() {
        final List<Archive> entities = Arrays.asList(archive, archive1, archive2);
        when(archiveDao.list(user)).thenReturn(entities);

        final List<ArchiveDto> expected = Arrays.asList(archiveDto, archiveDto1, archiveDto2);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Archive.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchiveDto.class)))).thenReturn(expected);

        final List<ArchiveDto> actual = sut.list(user.getUsername());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void archive_ShouldSaveArchivedPost() {
        when(Ref.create(any(Archive.class))).thenReturn(archiveRef);
        when(archiveRef.get()).thenReturn(archive);
        when(groupDao.find(user, group.getId())).thenReturn(Optional.of(group));
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(Optional.of(subscription));
        when(postDao.find(subscription, post.getId())).thenReturn(Optional.of(post));
        when(conversionService.convert(post, ArchivedPost.class)).thenReturn(archivedPost);

        sut.archive(user.getUsername(), group.getId(), subscription.getId(), post.getId(), archive.getId());

        verify(conversionService).convert(post, ArchivedPost.class);
        verify(archivedPostDao).save(archivedPostCaptor.capture());
        assertThat(archivedPostCaptor.getValue().getArchive()).isEqualTo(archive);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void archive_ShouldThrowException_WhenPostNotFound() {
        when(Ref.create(any(Archive.class))).thenReturn(archiveRef);
        when(archiveRef.get()).thenReturn(archive);
        when(groupDao.find(user, group.getId())).thenReturn(Optional.of(group));
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(Optional.of(subscription));
        when(postDao.find(subscription, post.getId())).thenReturn(Optional.empty());

        sut.archive(user.getUsername(), group.getId(), subscription.getId(), post.getId(), archive.getId());
    }

    @Test
    public void listPosts_ShouldReturnPostsForUser() {
        final ArchivedPostFilter filter = new ArchivedPostFilter(user.getUsername(), PAGE);
        final List<ArchivedPost> entities = Arrays.asList(archivedPost1, archivedPost2);
        when(archivedPostDao.list(user, filter.getPage())).thenReturn(entities);

        final List<ArchivedPostDto> expected = Arrays.asList(archivedPostDto1, archivedPostDto2);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPost.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPostDto.class)))).thenReturn(expected);

        final List<ArchivedPostDto> actual = sut.listPosts(filter);

        verify(archivedPostDao).list(user, filter.getPage());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void listPosts_ShouldReturnPostsForArchive() {
        final ArchivedPostFilter filter = new ArchivedPostFilter(user.getUsername(), archive.getId(), PAGE);
        final List<ArchivedPost> entities = Arrays.asList(archivedPost1, archivedPost2);
        when(archivedPostDao.list(archive, filter.getPage())).thenReturn(entities);

        final List<ArchivedPostDto> expected = Arrays.asList(archivedPostDto1, archivedPostDto2);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPost.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPostDto.class)))).thenReturn(expected);

        final List<ArchivedPostDto> actual = sut.listPosts(filter);

        verify(archivedPostDao).list(archive, filter.getPage());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deletePost_ShouldDeletePosts() {
        when(archivedPostDao.find(archive, archivedPost.getId())).thenReturn(Optional.of(archivedPost));

        sut.deletePost(user.getUsername(), archive.getId(), archivedPost.getId());

        verify(archivedPostDao).delete(archivedPost);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void deletePost_ShouldThrowException_WhenArchivedPostNotFound() {
        when(archivedPostDao.find(archive, archivedPost.getId())).thenReturn(Optional.empty());

        sut.deletePost(user.getUsername(), archive.getId(), archivedPost.getId());
    }

}
