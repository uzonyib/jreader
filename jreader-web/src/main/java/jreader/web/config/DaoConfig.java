package jreader.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;

@Configuration
@ComponentScan("jreader.dao")
public class DaoConfig {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Autowired
    private FeedDao feedDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private FeedStatDao feedStatDao;

    @Autowired
    private ArchiveDao archiveDao;

    @Autowired
    private ArchivedPostDao archivedPostDao;

    @Bean
    public DaoFacade daoFacade() {
        return DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).feedDao(feedDao).postDao(postDao)
                .feedStatDao(feedStatDao).archiveDao(archiveDao).archivedPostDao(archivedPostDao).build();
    }

}
