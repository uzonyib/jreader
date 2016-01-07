package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.SubscriptionDto;
import jreader.dto.GroupDto;
import jreader.services.ServiceException;
import jreader.services.GroupService;

public class GroupServiceImpl extends AbstractService implements GroupService {

    private PostDao postDao;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public GroupServiceImpl(final UserDao userDao, final GroupDao groupDao, final SubscriptionDao subscriptionDao,
            final PostDao postDao, final ConversionService conversionService, final BuilderFactory builderFactory) {
        super(userDao, groupDao, subscriptionDao);
        this.postDao = postDao;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
    }
    
    @Override
    public List<GroupDto> list(final String username) {
        final User user = this.getUser(username);
        final List<GroupDto> dtos = new ArrayList<GroupDto>();
        for (Group group : groupDao.list(user)) {
            final GroupDto dto = conversionService.convert(group, GroupDto.class);
            dto.setSubscriptions(new ArrayList<SubscriptionDto>());
            dtos.add(dto);
            final List<Subscription> subscriptions = subscriptionDao.list(group);
            int groupUnreadCount = 0;
            for (Subscription subscription : subscriptions) {
                final SubscriptionDto subscriptionDto = conversionService.convert(subscription, SubscriptionDto.class);
                final int unreadCount = postDao.countUnread(subscription);
                subscriptionDto.setUnreadCount(unreadCount);
                dto.getSubscriptions().add(subscriptionDto);
                groupUnreadCount += unreadCount;
            }
            dto.setUnreadCount(groupUnreadCount);
        }
        return dtos;
    }

    @Override
    public GroupDto create(final String username, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (groupDao.find(user, title) != null) {
            throw new ServiceException("Group already exists.", HttpStatus.CONFLICT);
        }
        final Group group = groupDao.save(builderFactory.createGroupBuilder().user(user).title(title)
                .order(groupDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(group, GroupDto.class);
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
