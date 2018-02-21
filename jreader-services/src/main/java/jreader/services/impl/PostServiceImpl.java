package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import jreader.dao.DaoFacade;
import jreader.dao.PostDao;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.PostDto;
import jreader.services.PostFilter;
import jreader.services.PostService;

@Service
public class PostServiceImpl extends AbstractService implements PostService {

    private PostDao postDao;

    private ConversionService conversionService;

    @Autowired
    public PostServiceImpl(final DaoFacade daoFacade, final ConversionService conversionService) {
        super(daoFacade.getUserDao(), daoFacade.getGroupDao(), daoFacade.getSubscriptionDao());
        this.postDao = daoFacade.getPostDao();
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
        if (post != null && !post.isBookmarked()) {
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
        if (post != null && post.isBookmarked()) {
            post.setBookmarked(false);
            postDao.save(post);
        }
    }

    @Override
    public List<PostDto> list(final PostFilter filter) {
        switch (filter.getParentType()) {
        case GROUP:
            return listForGroup(filter);
        case SUBSCRIPTION:
            return listForSubscription(filter);
        default:
            return listAll(filter);
        }
    }

    private List<PostDto> listAll(final PostFilter filter) {
        final User user = this.getUser(filter.getUsername());
        return convert(postDao.list(user, filter.getEntityFilter()));
    }

    private List<PostDto> listForGroup(final PostFilter filter) {
        final User user = this.getUser(filter.getUsername());
        final Group group = getGroup(user, filter.getGroupId());
        return convert(postDao.list(group, filter.getEntityFilter()));
    }

    private List<PostDto> listForSubscription(final PostFilter filter) {
        final User user = this.getUser(filter.getUsername());
        final Group group = this.getGroup(user, filter.getGroupId());
        final Subscription subscription = this.getSubscription(group, filter.getSubscriptionId());
        return convert(postDao.list(subscription, filter.getEntityFilter()));
    }

    @SuppressWarnings("unchecked")
    private List<PostDto> convert(final List<Post> posts) {
        return (List<PostDto>) conversionService.convert(posts,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)));
    }

}
