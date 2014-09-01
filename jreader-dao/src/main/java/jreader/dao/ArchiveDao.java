package jreader.dao;

import java.util.List;

import jreader.domain.Archive;
import jreader.domain.User;

public interface ArchiveDao extends OfyDao<Archive> {

    Archive find(User user, Long id);

    Archive find(User user, String title);

    List<Archive> list(User user);

    int getMaxOrder(User user);

}
