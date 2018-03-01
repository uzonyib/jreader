package jreader.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

public interface ArchivedPostDao extends OfyDao<ArchivedPost> {

    ArchivedPost find(Archive archive, Long id);

    List<ArchivedPost> list(User user, Pageable page);

    List<ArchivedPost> list(Archive archive, Pageable page);
    
    List<ArchivedPost> list(Archive archive);

}
