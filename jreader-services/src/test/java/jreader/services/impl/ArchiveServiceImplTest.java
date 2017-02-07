package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
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
import jreader.services.ServiceException;

public class ArchiveServiceImplTest extends ServiceTest {

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

        when(userDao.find(user.getUsername())).thenReturn(user);
        when(archiveDao.find(user, archive.getId())).thenReturn(archive);
    }

    @Test
    public void createArchive_ShouldCreateNewArchive_IfArchiveDoesNotExist() {
        when(Ref.create(any(User.class))).thenReturn(userRef);
        when(userRef.get()).thenReturn(user);
        final String archiveTitle = "new archive";
        final int maxOrder = 10;
        when(archiveDao.find(user, archiveTitle)).thenReturn(null);
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

    @Test
    public void createArchive_ShouldThrowException_IfArchiveAlreadyExists() {
        when(archiveDao.find(user, archive.getTitle())).thenReturn(archive);

        try {
            sut.createArchive(user.getUsername(), archive.getTitle());
            fail();
        } catch (ServiceException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        }

        verify(userDao).find(user.getUsername());
        verify(archiveDao).find(user, archive.getTitle());
        verifyNoMoreInteractions(archiveDao);
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

    @Test
    public void entitle_ShouldUpdateArchiveTitle_IfArchiveExists() {
        final String newTitle = "new title";

        sut.entitle(user.getUsername(), archive.getId(), newTitle);

        verify(userDao).find(user.getUsername());
        verify(archiveDao).save(archiveCaptor.capture());
        assertThat(archiveCaptor.getValue().getTitle()).isEqualTo(newTitle);
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
        when(groupDao.find(user, group.getId())).thenReturn(group);
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(subscription);
        when(postDao.find(subscription, post.getId())).thenReturn(post);
        when(conversionService.convert(post, ArchivedPost.class)).thenReturn(archivedPost);

        sut.archive(user.getUsername(), group.getId(), subscription.getId(), post.getId(), archive.getId());

        verify(conversionService).convert(post, ArchivedPost.class);
        verify(archivedPostDao).save(archivedPostCaptor.capture());
        assertThat(archivedPostCaptor.getValue().getArchive()).isEqualTo(archive);
    }

    @Test
    public void listPosts_ShouldReturnPostsForUser() {
        final ArchivedPostFilter filter = new ArchivedPostFilter(user.getUsername(), true, 0, 10);
        final List<ArchivedPost> entities = Arrays.asList(archivedPost1, archivedPost2);
        when(archivedPostDao.list(user, filter.getEntityFilter())).thenReturn(entities);

        final List<ArchivedPostDto> expected = Arrays.asList(archivedPostDto1, archivedPostDto2);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPost.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPostDto.class)))).thenReturn(expected);

        final List<ArchivedPostDto> actual = sut.listPosts(filter);

        verify(archivedPostDao).list(user, filter.getEntityFilter());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void listPosts_ShouldReturnPostsForArchive() {
        final ArchivedPostFilter filter = new ArchivedPostFilter(user.getUsername(), archive.getId(), true, 0, 10);
        final List<ArchivedPost> entities = Arrays.asList(archivedPost1, archivedPost2);
        when(archivedPostDao.list(archive, filter.getEntityFilter())).thenReturn(entities);

        final List<ArchivedPostDto> expected = Arrays.asList(archivedPostDto1, archivedPostDto2);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPost.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPostDto.class)))).thenReturn(expected);

        final List<ArchivedPostDto> actual = sut.listPosts(filter);

        verify(archivedPostDao).list(archive, filter.getEntityFilter());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deletePost_ShouldDeletePosts() {
        when(archivedPostDao.find(archive, archivedPost.getId())).thenReturn(archivedPost);

        sut.deletePost(user.getUsername(), archive.getId(), archivedPost.getId());

        verify(archivedPostDao).delete(archivedPost);
    }

}
