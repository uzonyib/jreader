package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.FeedDao;
import jreader.domain.Feed;

@Repository
public class FeedDaoImpl extends AbstractOfyDao<Feed> implements FeedDao {

    @Override
    protected LoadType<Feed> getLoadType() {
        return getOfy().load().type(Feed.class);
    }

    @Override
    public Optional<Feed> find(final String url) {
        return Optional.ofNullable(getLoadType().id(url).now());
    }

    @Override
    public List<Feed> listAll() {
        return getLoadType().list();
    }

}
