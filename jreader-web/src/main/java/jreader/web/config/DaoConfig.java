package jreader.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.PostDao;
import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.dao.impl.ArchiveDaoImpl;
import jreader.dao.impl.ArchivedPostDaoImpl;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.PostDaoImpl;
import jreader.dao.impl.FeedStatDaoImpl;
import jreader.dao.impl.SubscriptionDaoImpl;
import jreader.dao.impl.GroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;

@Configuration
public class DaoConfig {

    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }
    
    @Bean
    public GroupDao groupDao() {
        return new GroupDaoImpl();
    }
    
    @Bean
    public SubscriptionDao subscriptionDao() {
        return new SubscriptionDaoImpl();
    }
    
    @Bean
    public FeedDao feedDao() {
        return new FeedDaoImpl();
    }
    
    @Bean
    public PostDao postDao() {
        return new PostDaoImpl();
    }
    
    @Bean
    public FeedStatDao feedStatDao() {
        return new FeedStatDaoImpl();
    }
    
    @Bean
    public ArchiveDao archiveDao() {
        return new ArchiveDaoImpl();
    }
    
    @Bean
    public ArchivedPostDao archivedPostDao() {
        return new ArchivedPostDaoImpl();
    }
    
    @Bean
    public DaoFacade daoFacade() {
        return DaoFacade.builder().userDao(userDao()).groupDao(groupDao()).subscriptionDao(subscriptionDao()).feedDao(feedDao())
                .postDao(postDao()).feedStatDao(feedStatDao()).archiveDao(archiveDao()).archivedPostDao(archivedPostDao()).build();
    }
    
}
