package jreader.dao;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DaoFacade {

    private UserDao userDao;
    private GroupDao groupDao;
    private SubscriptionDao subscriptionDao;
    private FeedDao feedDao;
    private PostDao postDao;
    private FeedStatDao feedStatDao;
    private ArchiveDao archiveDao;
    private ArchivedPostDao archivedPostDao;

}
