package jreader.dao;

import java.util.List;

import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

public interface PostDao extends OfyDao<Post> {

    Post find(Subscription subscription, Long id);
    
    Post find(Subscription subscription, String uri, long publishDate);

    Post find(Subscription subscription, int ordinal);

    List<Post> list(User user, PostFilter filter);

    List<Post> list(Group group, PostFilter filter);

    List<Post> list(Subscription subscription, PostFilter filter);

    List<Post> listNotBookmarkedAndOlderThan(Subscription subscription, long date);

    List<Post> list(Subscription subscription);

    int countUnread(Subscription subscription);

}
