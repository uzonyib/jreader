package jreader.dao;

import java.util.List;

import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

public interface ArchivedPostDao extends OfyDao<ArchivedPost> {

    ArchivedPost find(Archive archive, Long id);

    List<ArchivedPost> list(User user, ArchivedPostFilter filter);

    List<ArchivedPost> list(Archive archive, ArchivedPostFilter filter);

}
