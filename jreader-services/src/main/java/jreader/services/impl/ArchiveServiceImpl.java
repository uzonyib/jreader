package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.DaoFacade;
import jreader.dao.PostDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedPostDto;
import jreader.services.ArchiveService;
import jreader.services.ArchivedPostFilter;
import jreader.services.ServiceException;

@Service
public class ArchiveServiceImpl extends AbstractService implements ArchiveService {

    private PostDao postDao;
    private ArchiveDao archiveDao;
    private ArchivedPostDao archivedPostDao;

    private ConversionService conversionService;

    @Autowired
    public ArchiveServiceImpl(final DaoFacade daoFacade, final ConversionService conversionService) {
        super(daoFacade.getUserDao(), daoFacade.getGroupDao(), daoFacade.getSubscriptionDao());
        this.postDao = daoFacade.getPostDao();
        this.archiveDao = daoFacade.getArchiveDao();
        this.archivedPostDao = daoFacade.getArchivedPostDao();
        this.conversionService = conversionService;
    }

    private Archive getArchive(final User user, final Long id) {
        final Archive archive = archiveDao.find(user, id);
        if (archive == null) {
            throw new ServiceException("Archive not found, ID " + id, HttpStatus.NOT_FOUND);
        }
        return archive;
    }

    @Override
    public ArchiveDto createArchive(final String username, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Archive title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (archiveDao.find(user, title) != null) {
            throw new ServiceException("Archive already exists.", HttpStatus.CONFLICT);
        }
        final Archive archive = archiveDao.save(new Archive.Builder().user(user).title(title).order(archiveDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(archive, ArchiveDto.class);
    }

    @Override
    public void deleteArchive(final String username, final Long archiveId) {
        final User user = this.getUser(username);
        final Archive archive = this.getArchive(user, archiveId);
        final List<ArchivedPost> archivedPosts = archivedPostDao.list(archive);
        archivedPostDao.deleteAll(archivedPosts);
        archiveDao.delete(archive);
    }

    @Override
    public void moveUp(final String username, final Long archiveId) {
        final User user = this.getUser(username);

        final List<Archive> archives = archiveDao.list(user);
        Integer index = null;
        for (int i = 0; i < archives.size(); ++i) {
            if (archives.get(i).getId().equals(archiveId)) {
                index = i;
            }
        }

        if (index == null) {
            return;
        }
        if (index == 0) {
            throw new ServiceException("Cannot move first archive up.", HttpStatus.BAD_REQUEST);
        }

        swap(archives.get(index - 1), archives.get(index));
    }

    @Override
    public void moveDown(final String username, final Long archiveId) {
        final User user = this.getUser(username);

        final List<Archive> archives = archiveDao.list(user);
        Integer index = null;
        for (int i = 0; i < archives.size(); ++i) {
            if (archives.get(i).getId().equals(archiveId)) {
                index = i;
            }
        }

        if (index == null) {
            return;
        }
        if (index == archives.size() - 1) {
            throw new ServiceException("Cannot move last archive down.", HttpStatus.BAD_REQUEST);
        }

        swap(archives.get(index), archives.get(index + 1));
    }

    private void swap(final Archive archive1, final Archive archive2) {
        final int order = archive1.getOrder();
        archive1.setOrder(archive2.getOrder());
        archive2.setOrder(order);

        final List<Archive> updatedArchives = new ArrayList<Archive>();
        updatedArchives.add(archive1);
        updatedArchives.add(archive2);

        archiveDao.saveAll(updatedArchives);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ArchiveDto> list(final String username) {
        final User user = this.getUser(username);
        return (List<ArchiveDto>) conversionService.convert(archiveDao.list(user),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Archive.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchiveDto.class)));
    }

    @Override
    public void entitle(final String username, final Long archiveId, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Archive title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (archiveDao.find(user, title) != null) {
            throw new ServiceException("Archive with this title already exists.", HttpStatus.CONFLICT);
        }
        final Archive archive = this.getArchive(user, archiveId);

        archive.setTitle(title);
        archiveDao.save(archive);
    }

    @Override
    public void archive(final String username, final Long groupId, final Long subscriptionId, final Long postId, final Long archiveId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final Post post = postDao.find(subscription, postId);
        final Archive archive = this.getArchive(user, archiveId);

        final ArchivedPost entity = conversionService.convert(post, ArchivedPost.class);
        entity.setArchive(archive);
        archivedPostDao.save(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ArchivedPostDto> listPosts(final ArchivedPostFilter filter) {
        final User user = this.getUser(filter.getUsername());
        final List<ArchivedPost> posts;
        if (filter.getArchiveId() == null) {
            posts = archivedPostDao.list(user, filter.getEntityFilter());
        } else {
            final Archive archive = this.getArchive(user, filter.getArchiveId());
            posts = archivedPostDao.list(archive, filter.getEntityFilter());
        }
        
        return (List<ArchivedPostDto>) conversionService.convert(posts,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPost.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ArchivedPostDto.class)));
    }

    @Override
    public void deletePost(final String username, final Long archiveId, final Long postId) {
        final User user = this.getUser(username);
        final Archive archive = this.getArchive(user, archiveId);
        final ArchivedPost post = archivedPostDao.find(archive, postId);
        archivedPostDao.delete(post);
    }

}
