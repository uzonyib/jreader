package jreader.dao;

public class DaoFacade {

    private final UserDao userDao;
    private final GroupDao groupDao;
    private final SubscriptionDao subscriptionDao;
    private final FeedDao feedDao;
    private final PostDao postDao;
    private final FeedStatDao feedStatDao;
    private final ArchiveDao archiveDao;
    private final ArchivedPostDao archivedPostDao;

    public DaoFacade(final Builder builder) {
        this.userDao = builder.userDao;
        this.groupDao = builder.groupDao;
        this.subscriptionDao = builder.subscriptionDao;
        this.feedDao = builder.feedDao;
        this.postDao = builder.postDao;
        this.feedStatDao = builder.feedStatDao;
        this.archiveDao = builder.archiveDao;
        this.archivedPostDao = builder.archivedPostDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public SubscriptionDao getSubscriptionDao() {
        return subscriptionDao;
    }

    public FeedDao getFeedDao() {
        return feedDao;
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public FeedStatDao getFeedStatDao() {
        return feedStatDao;
    }

    public ArchiveDao getArchiveDao() {
        return archiveDao;
    }

    public ArchivedPostDao getArchivedPostDao() {
        return archivedPostDao;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UserDao userDao;
        private GroupDao groupDao;
        private SubscriptionDao subscriptionDao;
        private FeedDao feedDao;
        private PostDao postDao;
        private FeedStatDao feedStatDao;
        private ArchiveDao archiveDao;
        private ArchivedPostDao archivedPostDao;

        public Builder userDao(final UserDao userDao) {
            this.userDao = userDao;
            return this;
        }

        public Builder groupDao(final GroupDao groupDao) {
            this.groupDao = groupDao;
            return this;
        }

        public Builder subscriptionDao(final SubscriptionDao subscriptionDao) {
            this.subscriptionDao = subscriptionDao;
            return this;
        }

        public Builder feedDao(final FeedDao feedDao) {
            this.feedDao = feedDao;
            return this;
        }

        public Builder postDao(final PostDao postDao) {
            this.postDao = postDao;
            return this;
        }

        public Builder feedStatDao(final FeedStatDao feedStatDao) {
            this.feedStatDao = feedStatDao;
            return this;
        }

        public Builder archiveDao(final ArchiveDao archiveDao) {
            this.archiveDao = archiveDao;
            return this;
        }

        public Builder archivedPostDao(final ArchivedPostDao archivedPostDao) {
            this.archivedPostDao = archivedPostDao;
            return this;
        }
        
        public DaoFacade build() {
            return new DaoFacade(this);
        }

    }

}
