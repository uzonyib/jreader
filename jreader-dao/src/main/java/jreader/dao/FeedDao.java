package jreader.dao;

import java.util.List;

import jreader.domain.Feed;

public interface FeedDao extends OfyDao<Feed> {

    Feed find(String url);

    List<Feed> listAll();

}
