package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.ServiceException;
import jreader.services.SubscriptionGroupService;

public class SubscriptionGroupServiceImpl extends AbstractService implements SubscriptionGroupService {

    private FeedEntryDao feedEntryDao;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public SubscriptionGroupServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedEntryDao feedEntryDao, final ConversionService conversionService, final BuilderFactory builderFactory) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedEntryDao = feedEntryDao;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
    }
    
    @Override
    public List<SubscriptionGroupDto> list(final String username) {
        final User user = this.getUser(username);
        final List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
        for (SubscriptionGroup subscriptionGroup : subscriptionGroupDao.list(user)) {
            final SubscriptionGroupDto dto = conversionService.convert(subscriptionGroup, SubscriptionGroupDto.class);
            dto.setSubscriptions(new ArrayList<SubscriptionDto>());
            dtos.add(dto);
            final List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
            int groupUnreadCount = 0;
            for (Subscription subscription : subscriptions) {
                final SubscriptionDto subscriptionDto = conversionService.convert(subscription, SubscriptionDto.class);
                final int unreadCount = feedEntryDao.countUnread(subscription);
                subscriptionDto.setUnreadCount(unreadCount);
                dto.getSubscriptions().add(subscriptionDto);
                groupUnreadCount += unreadCount;
            }
            dto.setUnreadCount(groupUnreadCount);
        }
        return dtos;
    }

    @Override
    public SubscriptionGroupDto create(final String username, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (subscriptionGroupDao.find(user, title) != null) {
            throw new ServiceException("Group already exists.", HttpStatus.CONFLICT);
        }
        final SubscriptionGroup group = subscriptionGroupDao.save(builderFactory.createGroupBuilder().user(user).title(title)
                .order(subscriptionGroupDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(group, SubscriptionGroupDto.class);
    }

    @Override
    public void entitle(final String username, final Long subscriptionGroupId, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (subscriptionGroupDao.find(user, title) != null) {
            throw new ServiceException("Group with this title already exists.", HttpStatus.CONFLICT);
        }
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        subscriptionGroup.setTitle(title);
        subscriptionGroupDao.save(subscriptionGroup);
    }

    @Override
    public void moveUp(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);

        final List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + subscriptionGroupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == 0) {
            throw new ServiceException("Cannot move first group up.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptionGroups.get(groupIndex - 1), subscriptionGroups.get(groupIndex));
    }

    @Override
    public void moveDown(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);

        final List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + subscriptionGroupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == subscriptionGroups.size() - 1) {
            throw new ServiceException("Cannot move last group down.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptionGroups.get(groupIndex), subscriptionGroups.get(groupIndex + 1));
    }

    private void swap(final SubscriptionGroup group1, final SubscriptionGroup group2) {
        final int order = group1.getOrder();
        group1.setOrder(group2.getOrder());
        group2.setOrder(order);

        final List<SubscriptionGroup> updatedGroups = new ArrayList<SubscriptionGroup>();
        updatedGroups.add(group1);
        updatedGroups.add(group2);

        subscriptionGroupDao.saveAll(updatedGroups);
    }
    
    @Override
    public void delete(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);
        subscriptionGroupDao.delete(subscriptionGroup);
    }

}
