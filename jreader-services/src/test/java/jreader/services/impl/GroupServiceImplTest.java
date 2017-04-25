package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.objectify.Ref;

import jreader.dao.DaoFacade;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.GroupDto;
import jreader.dto.SubscriptionDto;
import jreader.services.ServiceException;

@PowerMockIgnore("org.springframework.http.*")
public class GroupServiceImplTest extends ServiceTest {

    private GroupServiceImpl sut;

    @Mock
    private UserDao userDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private PostDao postDao;
    @Mock
    private ConversionService conversionService;

    private User user;
    private Group group;
    private Group group1;
    private Group group2;

    @Mock
    private Ref<User> userRef;
    @Mock
    private Subscription subscription;
    @Mock
    private Subscription subscription1;
    @Mock
    private Subscription subscription2;

    private GroupDto groupDto;
    private GroupDto groupDto1;
    private GroupDto groupDto2;

    private SubscriptionDto subscriptionDto;
    private SubscriptionDto subscriptionDto1;
    private SubscriptionDto subscriptionDto2;

    @Captor
    private ArgumentCaptor<Group> groupCaptor;
    @Captor
    private ArgumentCaptor<List<Group>> groupListCaptor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);

        when(Ref.create(user)).thenReturn(userRef);
        when(userRef.get()).thenReturn(user);

        group = Group.builder().user(user).id(0L).title("group 0").order(0).build();
        group1 = Group.builder().user(user).id(1L).title("group 1").order(1).build();
        group2 = Group.builder().user(user).id(2L).title("group 2").order(2).build();

        groupDto = GroupDto.builder().id(0L).title("group 0").order(0).build();
        groupDto1 = GroupDto.builder().id(1L).title("group 1").order(1).build();
        groupDto2 = GroupDto.builder().id(2L).title("group 2").order(2).build();

        subscriptionDto = new SubscriptionDto("0", "subscription", null, 1000L, 0);
        subscriptionDto1 = new SubscriptionDto("1", "subscription1", null, 2000L, 1);
        subscriptionDto2 = new SubscriptionDto("2", "subscription2", null, 3000L, 2);

        sut = new GroupServiceImpl(DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).postDao(postDao).build(),
                conversionService);

        when(userDao.find(user.getUsername())).thenReturn(user);
    }

    @Test
    public void create_ShouldCreateGroup_IfGroupDoesNotExist() {
        final String groupTitle = "new group";
        final int maxOrder = 10;
        when(groupDao.find(user, groupTitle)).thenReturn(null);
        when(groupDao.getMaxOrder(user)).thenReturn(maxOrder);
        when(groupDao.save(any(Group.class))).thenReturn(group);
        when(conversionService.convert(group, GroupDto.class)).thenReturn(groupDto);

        final GroupDto actual = sut.create(user.getUsername(), groupTitle);

        assertThat(actual).isEqualTo(groupDto);

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, groupTitle);
        verify(groupDao).getMaxOrder(user);
        verify(groupDao).save(groupCaptor.capture());

        assertThat(groupCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(groupCaptor.getValue().getTitle()).isEqualTo(groupTitle);
        assertThat(groupCaptor.getValue().getOrder()).isEqualTo(maxOrder + 1);
    }

    @Test
    public void create_ShouldThrowException_IfGroupAlreadyExists() {
        when(groupDao.find(user, group.getTitle())).thenReturn(group);

        try {
            sut.create(user.getUsername(), group.getTitle());
            fail();
        } catch (ServiceException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        }

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, group.getTitle());
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    public void update_ShouldUpdateTitle_IfGroupExist() {
        final GroupDto groupToUpdate = GroupDto.builder().id(group.getId()).title("new group title").order(20).build();
        when(groupDao.find(user, groupToUpdate.getTitle())).thenReturn(null);
        when(groupDao.find(user, groupToUpdate.getId())).thenReturn(group);
        when(groupDao.save(any(Group.class))).thenReturn(group);
        when(conversionService.convert(group, GroupDto.class)).thenReturn(groupDto);

        final GroupDto actual = sut.update(user.getUsername(), groupToUpdate);

        assertThat(actual).isEqualTo(groupDto);

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, groupToUpdate.getTitle());
        verify(groupDao).find(user, groupToUpdate.getId());
        verify(groupDao).save(groupCaptor.capture());

        assertThat(groupCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(groupCaptor.getValue().getTitle()).isEqualTo(groupToUpdate.getTitle());
        assertThat(groupCaptor.getValue().getOrder()).isEqualTo(group.getOrder());
    }

    @Test
    public void update_ShouldThrowException_IfTitleAlreadyExists() {
        final GroupDto groupToUpdate = GroupDto.builder().id(group.getId()).title(group1.getTitle()).order(group.getOrder()).build();
        when(groupDao.find(user, groupToUpdate.getTitle())).thenReturn(group1);

        try {
            sut.update(user.getUsername(), groupToUpdate);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        }

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, groupToUpdate.getTitle());
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    public void delete_ShouldDeleteGroup_IfGroupExists() {
        when(groupDao.find(user, group.getId())).thenReturn(group);

        sut.delete(user.getUsername(), group.getId());

        verify(userDao).find(user.getUsername());
        verify(groupDao).delete(group);
    }

    @Test
    public void entitle_ShouldUpdateGroupTitle_IfGroupExists() {
        when(groupDao.find(user, group.getId())).thenReturn(group);
        final String newTitle = "new title";

        sut.entitle(user.getUsername(), group.getId(), newTitle);

        verify(userDao).find(user.getUsername());
        verify(groupDao).save(groupCaptor.capture());
        assertThat(groupCaptor.getValue().getTitle()).isEqualTo(newTitle);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void reorder_ShouldThrowException_IfGroupCountDoesNotMatch() {
        when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));

        sut.reorder(user.getUsername(), Arrays.asList(groupDto, groupDto1));
    }

    @Test(expectedExceptions = ServiceException.class)
    public void reorder_ShouldThrowException_IfTitlesDoNotMatch() {
        when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
        groupDto2.setId(100L);

        sut.reorder(user.getUsername(), Arrays.asList(groupDto, groupDto1, groupDto2));
    }

    @Test
    public void reorder_ShouldSwapGroups() {
        when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));

        sut.reorder(user.getUsername(), Arrays.asList(groupDto, groupDto2, groupDto1));

        verify(groupDao).saveAll(groupListCaptor.capture());
        assertThat(groupListCaptor.getValue().get(0).getId()).isEqualTo(group1.getId());
        assertThat(groupListCaptor.getValue().get(0).getOrder()).isEqualTo(2);
        assertThat(groupListCaptor.getValue().get(1).getId()).isEqualTo(group2.getId());
        assertThat(groupListCaptor.getValue().get(1).getOrder()).isEqualTo(1);
    }

    @Test
    public void reorder_ShouldReinitializeGroupOrders() {
        group.setOrder(1);
        group1.setOrder(2);
        group2.setOrder(3);
        when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));

        sut.reorder(user.getUsername(), Arrays.asList(groupDto, groupDto1, groupDto2));

        verify(groupDao).saveAll(groupListCaptor.capture());
        assertThat(groupListCaptor.getValue().get(0).getId()).isEqualTo(group.getId());
        assertThat(groupListCaptor.getValue().get(0).getOrder()).isEqualTo(0);
        assertThat(groupListCaptor.getValue().get(1).getId()).isEqualTo(group1.getId());
        assertThat(groupListCaptor.getValue().get(1).getOrder()).isEqualTo(1);
        assertThat(groupListCaptor.getValue().get(2).getId()).isEqualTo(group2.getId());
        assertThat(groupListCaptor.getValue().get(2).getOrder()).isEqualTo(2);
    }

    @Test
    public void reorder_ShouldDoNothing_IfOrdersAreNotChanged() {
        when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));

        sut.reorder(user.getUsername(), Arrays.asList(groupDto, groupDto1, groupDto2));

        verify(groupDao).list(user);
        verifyNoMoreInteractions(groupDao);
    }

    @Test
    public void list_ShouldReturnGroupListWithUnreadCounts() {
        final List<Group> groups = Arrays.asList(group, group1, group2);
        when(groupDao.list(user)).thenReturn(groups);
        final List<Subscription> subscriptions = Arrays.asList(subscription);
        when(subscriptionDao.list(group)).thenReturn(subscriptions);
        final List<Subscription> subscriptions1 = Arrays.asList(subscription1, subscription2);
        when(subscriptionDao.list(group1)).thenReturn(subscriptions1);
        final List<Subscription> subscriptions2 = Collections.emptyList();
        when(subscriptionDao.list(group2)).thenReturn(subscriptions2);
        when(postDao.countUnread(subscription)).thenReturn(1);
        when(postDao.countUnread(subscription1)).thenReturn(2);
        when(postDao.countUnread(subscription2)).thenReturn(3);

        final List<GroupDto> expected = Arrays.asList(groupDto, groupDto1, groupDto2);
        when(conversionService.convert(groups,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Group.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(GroupDto.class)))).thenReturn(expected);

        final List<SubscriptionDto> dtos = Arrays.asList(subscriptionDto);
        when(conversionService.convert(subscriptions,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)))).thenReturn(dtos);
        final List<SubscriptionDto> dtos1 = Arrays.asList(subscriptionDto1, subscriptionDto2);
        when(conversionService.convert(subscriptions1,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)))).thenReturn(dtos1);
        final List<SubscriptionDto> dtos2 = Collections.emptyList();
        when(conversionService.convert(subscriptions2,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)))).thenReturn(dtos2);

        final List<GroupDto> actual = sut.list(user.getUsername());

        assertThat(actual).isEqualTo(expected);

        assertThat(groupDto.getSubscriptions()).isSameAs(dtos);
        assertThat(groupDto1.getSubscriptions()).isSameAs(dtos1);
        assertThat(groupDto2.getSubscriptions()).isSameAs(dtos2);

        assertThat(groupDto.getUnreadCount()).isEqualTo(1);
        assertThat(groupDto1.getUnreadCount()).isEqualTo(5);
        assertThat(groupDto2.getUnreadCount()).isEqualTo(0);

        assertThat(subscriptionDto.getUnreadCount()).isEqualTo(1);
        assertThat(subscriptionDto1.getUnreadCount()).isEqualTo(2);
        assertThat(subscriptionDto2.getUnreadCount()).isEqualTo(3);
    }

}
