package jreader.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedEntryDao;
import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.dao.impl.ArchiveDaoImpl;
import jreader.dao.impl.ArchivedEntryDaoImpl;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.FeedEntryDaoImpl;
import jreader.dao.impl.FeedStatDaoImpl;
import jreader.dao.impl.SubscriptionDaoImpl;
import jreader.dao.impl.SubscriptionGroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;

@Configuration
public class DaoConfig {

    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }
    
    @Bean
    public SubscriptionGroupDao subscriptionGroupDao() {
        return new SubscriptionGroupDaoImpl();
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
    public FeedEntryDao feedEntryDao() {
        return new FeedEntryDaoImpl();
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
    public ArchivedEntryDao archivedEntryDao() {
        return new ArchivedEntryDaoImpl();
    }
    
}
