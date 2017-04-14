package jreader.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;

import jreader.dao.DaoFacade;
import jreader.dao.PostDao;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.GroupDto;
import jreader.dto.SubscriptionDto;
import jreader.services.GroupService;
import jreader.services.ServiceException;

public class GroupServiceImpl extends AbstractService implements GroupService {

    private PostDao postDao;

    private ConversionService conversionService;

    public GroupServiceImpl(final DaoFacade daoFacade, final ConversionService conversionService) {
        super(daoFacade.getUserDao(), daoFacade.getGroupDao(), daoFacade.getSubscriptionDao());
        this.postDao = daoFacade.getPostDao();
        this.conversionService = conversionService;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<GroupDto> list(final String username) {
        final User user = this.getUser(username);
        final List<Group> groups = groupDao.list(user);
        final List<GroupDto> groupDtos = (List<GroupDto>) conversionService.convert(groups,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Group.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(GroupDto.class)));
        
        for (int i = 0; i < groups.size(); ++i) {
            final List<Subscription> subscriptions = subscriptionDao.list(groups.get(i));
            final List<SubscriptionDto> subscriptionDtos = (List<SubscriptionDto>) conversionService.convert(subscriptions,
                    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)));
            groupDtos.get(i).setSubscriptions(subscriptionDtos);
            
            int groupUnreadCount = 0;
            for (int j = 0; j < subscriptions.size(); ++j) {
                final int unreadCount = postDao.countUnread(subscriptions.get(j));
                subscriptionDtos.get(j).setUnreadCount(unreadCount);
                groupUnreadCount += unreadCount;
            }
            groupDtos.get(i).setUnreadCount(groupUnreadCount);
        }
        
        return groupDtos;
    }

    @Override
    public GroupDto create(final String username, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Invalid group title.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (groupDao.find(user, title) != null) {
            throw new ServiceException("Group already exists.", HttpStatus.CONFLICT);
        }

        final Group entity = groupDao.save(Group.builder().user(user).title(title).order(groupDao.getMaxOrder(user) + 1).build());

        final GroupDto result = conversionService.convert(entity, GroupDto.class);
        result.setSubscriptions(Collections.<SubscriptionDto>emptyList());
        return result;
    }

    @Override
    public GroupDto update(final String username, final GroupDto group) {
        if (group.getTitle() == null || "".equals(group.getTitle())) {
            throw new ServiceException("Invalid group title.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (groupDao.find(user, group.getTitle()) != null) {
            throw new ServiceException("Group with this title already exists.", HttpStatus.CONFLICT);
        }

        final Group entity = this.getGroup(user, group.getId());
        entity.setTitle(group.getTitle());

        final GroupDto result = conversionService.convert(groupDao.save(entity), GroupDto.class);
        result.setSubscriptions(Collections.<SubscriptionDto>emptyList());
        return result;
    }

    @Override
    public void entitle(final String username, final Long groupId, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (groupDao.find(user, title) != null) {
            throw new ServiceException("Group with this title already exists.", HttpStatus.CONFLICT);
        }
        final Group group = this.getGroup(user, groupId);

        group.setTitle(title);
        groupDao.save(group);
    }

    @Override
    public void moveUp(final String username, final Long groupId) {
        final User user = this.getUser(username);

        final List<Group> groups = groupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < groups.size(); ++i) {
            if (groups.get(i).getId().equals(groupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + groupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == 0) {
            throw new ServiceException("Cannot move first group up.", HttpStatus.BAD_REQUEST);
        }

        swap(groups.get(groupIndex - 1), groups.get(groupIndex));
    }

    @Override
    public void moveDown(final String username, final Long groupId) {
        final User user = this.getUser(username);

        final List<Group> groups = groupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < groups.size(); ++i) {
            if (groups.get(i).getId().equals(groupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + groupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == groups.size() - 1) {
            throw new ServiceException("Cannot move last group down.", HttpStatus.BAD_REQUEST);
        }

        swap(groups.get(groupIndex), groups.get(groupIndex + 1));
    }

    private void swap(final Group group1, final Group group2) {
        final int order = group1.getOrder();
        group1.setOrder(group2.getOrder());
        group2.setOrder(order);

        final List<Group> updatedGroups = new ArrayList<Group>();
        updatedGroups.add(group1);
        updatedGroups.add(group2);

        groupDao.saveAll(updatedGroups);
    }

    @Override
    public void delete(final String username, final Long groupId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        groupDao.delete(group);
    }

}
