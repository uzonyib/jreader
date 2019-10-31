package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.Feed;

public interface FeedDao extends OfyDao<Feed> {

    Optional<Feed> find(String url);

    List<Feed> listAll();

}
