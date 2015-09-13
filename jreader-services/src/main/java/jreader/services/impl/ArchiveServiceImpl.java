package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedEntryDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.BuilderFactory;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedEntryDto;
import jreader.services.ArchiveService;
import jreader.services.ArchivedEntryFilterData;
import jreader.services.ServiceException;

public class ArchiveServiceImpl extends AbstractService implements ArchiveService {

    private FeedEntryDao feedEntryDao;
    private ArchiveDao archiveDao;
    private ArchivedEntryDao archivedEntryDao;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public ArchiveServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedEntryDao feedEntryDao, final ArchiveDao archiveDao, final ArchivedEntryDao archivedEntryDao, final ConversionService conversionService,
            final BuilderFactory builderFactory) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedEntryDao = feedEntryDao;
        this.archiveDao = archiveDao;
        this.archivedEntryDao = archivedEntryDao;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
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
        final Archive archive = archiveDao.save(builderFactory.createArchiveBuilder().user(user).title(title).order(archiveDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(archive, ArchiveDto.class);
    }

    @Override
    public void deleteArchive(final String username, final Long archiveId) {
        final User user = this.getUser(username);
        final Archive archive = this.getArchive(user, archiveId);
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
    public List<ArchiveDto> list(final String username) {
        final User user = this.getUser(username);
        final List<ArchiveDto> dtos = new ArrayList<ArchiveDto>();
        for (final Archive archive : archiveDao.list(user)) {
            dtos.add(conversionService.convert(archive, ArchiveDto.class));
        }
        return dtos;
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
    public void archive(final String username, final Long groupId, final Long subscriptionId, final Long entryId, final Long archiveId) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final FeedEntry feedEntry = feedEntryDao.find(subscription, entryId);
        final Archive archive = this.getArchive(user, archiveId);

        final ArchivedEntry entity = conversionService.convert(feedEntry, ArchivedEntry.class);
        entity.setArchive(archive);
        archivedEntryDao.save(entity);
    }

    @Override
    public List<ArchivedEntryDto> listEntries(final ArchivedEntryFilterData filterData) {
        final User user = this.getUser(filterData.getUsername());
        final List<ArchivedEntry> entries;
        if (filterData.getArchiveId() == null) {
            entries = archivedEntryDao.list(user, filterData);
        } else {
            final Archive archive = this.getArchive(user, filterData.getArchiveId());
            entries = archivedEntryDao.list(archive, filterData);
        }
        final List<ArchivedEntryDto> dtos = new ArrayList<ArchivedEntryDto>();
        for (final ArchivedEntry entry : entries) {
            dtos.add(conversionService.convert(entry, ArchivedEntryDto.class));
        }
        return dtos;
    }

    @Override
    public void deleteEntry(final String username, final Long archiveId, final Long entryId) {
        final User user = this.getUser(username);
        final Archive archive = this.getArchive(user, archiveId);
        final ArchivedEntry entry = archivedEntryDao.find(archive, entryId);
        archivedEntryDao.delete(entry);
    }

}
