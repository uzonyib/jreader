package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jreader.dao.PostDao;
import jreader.dao.PostFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.PostDto;
import jreader.services.PostFilterData;
import jreader.services.PostService;

import org.springframework.core.convert.ConversionService;

public class PostServiceImpl extends AbstractService implements PostService {

    private PostDao postDao;

    private ConversionService conversionService;

    public PostServiceImpl(final UserDao userDao, final GroupDao groupDao, final SubscriptionDao subscriptionDao,
            final PostDao postDao, final ConversionService conversionService) {
        super(userDao, groupDao, subscriptionDao);
        this.postDao = postDao;
        this.conversionService = conversionService;
    }

    @Override
    public void markRead(final String username, final Map<Long, Map<Long, List<Long>>> ids) {
        if (ids == null) {
            return;
        }

        final User user = this.getUser(username);

        final List<Post> postsToSave = new ArrayList<Post>();
        for (final Entry<Long, Map<Long, List<Long>>> groupEntry : ids.entrySet()) {
            final Group group = this.getGroup(user, groupEntry.getKey());
            for (final Entry<Long, List<Long>> subscriptionEntry : groupEntry.getValue().entrySet()) {
                final Subscription subscription = this.getSubscription(group, subscriptionEntry.getKey());
                for (final Long postId : subscriptionEntry.getValue()) {
                    final Post post = postDao.find(subscription, postId);
                    if (post != null && !post.isRead()) {
                        post.setRead(true);
                        postsToSave.add(post);
                    }
                }
            }
        }
        postDao.saveAll(postsToSave);
    }

    @Override
    public void bookmark(final String username, final Long groupId, final Long subscriptionId, final Long postId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final Post post = postDao.find(subscription, postId);
        if (post != null && !post.isBookMarked()) {
            post.setBookmarked(true);
            postDao.save(post);
        }
    }

    @Override
    public void deleteBookmark(final String username, final Long groupId, final Long subscriptionId, final Long postId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final Post post = postDao.find(subscription, postId);
        if (post != null && post.isBookMarked()) {
            post.setBookmarked(false);
            postDao.save(post);
        }
    }

    @Override
    public List<PostDto> list(final PostFilterData filterData) {
        switch (filterData.getVertical()) {
        case GROUP:
            return listForGroup(filterData.getUsername(), filterData.getGroupId(), filterData);
        case SUBSCRIPTION:
            return listForSubscription(filterData.getUsername(), filterData.getGroupId(), filterData.getSubscriptionId(), filterData);
        default:
            return listAll(filterData.getUsername(), filterData);
        }
    }

    private List<PostDto> listAll(final String username, final PostFilter filter) {
        final User user = this.getUser(username);
        return convert(postDao.list(user, filter));
    }

    private List<PostDto> listForGroup(final String username, final Long groupId, final PostFilter filter) {
        final User user = this.getUser(username);
        final Group group = getGroup(user, groupId);
        return convert(postDao.list(group, filter));
    }

    private List<PostDto> listForSubscription(final String username, final Long groupId, final Long subscriptionId,
            final PostFilter filter) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);
        return convert(postDao.list(subscription, filter));
    }

    private List<PostDto> convert(final List<Post> posts) {
        final List<PostDto> dtos = new ArrayList<PostDto>();
        for (final Post post : posts) {
            dtos.add(conversionService.convert(post, PostDto.class));
        }
        return dtos;
    }

}
