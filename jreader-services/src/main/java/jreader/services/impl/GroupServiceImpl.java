package jreader.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jreader.dao.DaoFacade;
import jreader.dao.PostDao;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.GroupDto;
import jreader.dto.SubscriptionDto;
import jreader.services.GroupService;
import jreader.services.ServiceException;

@Service
public class GroupServiceImpl extends AbstractService implements GroupService {

    private PostDao postDao;

    private ConversionService conversionService;

    @Autowired
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
    public void reorder(final String username, final List<GroupDto> groups) {
        final User user = this.getUser(username);

        final List<Group> entities = groupDao.list(user);
        if (groups.size() != entities.size()) {
            throw new ServiceException("Group count does not match.", HttpStatus.BAD_REQUEST);
        }

        final Map<Long, GroupDto> groupsById = new HashMap<>();
        for (int i = 0; i < groups.size(); ++i) {
            final GroupDto group = groups.get(i);
            group.setOrder(i);
            groupsById.put(group.getId(), group);
        }

        final List<Group> entitiesToSave = new ArrayList<>();
        for (final Group group : entities) {
            final GroupDto groupById = groupsById.get(group.getId());
            if (groupById == null) {
                throw new ServiceException("Group " + group.getId() + " (" + group.getTitle() + ") not found.", HttpStatus.BAD_REQUEST);
            }
            final int order = groupById.getOrder();
            if (group.getOrder() != order) {
                group.setOrder(order);
                entitiesToSave.add(group);
            }
        }

        if (!entitiesToSave.isEmpty()) {
            groupDao.saveAll(entitiesToSave);
        }
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
    public void delete(final String username, final Long groupId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        groupDao.delete(group);
    }

}
