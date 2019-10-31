package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.Archive;
import jreader.domain.User;

public interface ArchiveDao extends OfyDao<Archive> {

    Optional<Archive> find(User user, Long id);

    Optional<Archive> find(User user, String title);

    List<Archive> list(User user);

    int getMaxOrder(User user);

}
