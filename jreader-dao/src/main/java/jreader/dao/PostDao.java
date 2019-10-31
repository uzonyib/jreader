package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

public interface PostDao extends OfyDao<Post> {

    Optional<Post> find(Subscription subscription, Long id);
    
    Optional<Post> find(Subscription subscription, String uri, long publishDate);

    Optional<Post> find(Subscription subscription, int ordinal);

    List<Post> list(User user, PostFilter filter);

    List<Post> list(Group group, PostFilter filter);

    List<Post> list(Subscription subscription, PostFilter filter);

    List<Post> listNotBookmarkedAndOlderThan(Subscription subscription, long date);

    List<Post> list(Subscription subscription);

    int countUnread(Subscription subscription);

}
