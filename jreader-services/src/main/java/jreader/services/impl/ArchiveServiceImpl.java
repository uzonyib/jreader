package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedEntryDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.impl.EntityFactory;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedEntryDto;
import jreader.services.ArchiveService;
import jreader.services.ArchivedEntryFilterData;
import jreader.services.ServiceException;
import jreader.services.ServiceStatus;

import org.springframework.core.convert.ConversionService;

public class ArchiveServiceImpl extends AbstractService implements ArchiveService {

	private FeedEntryDao feedEntryDao;
	private ArchiveDao archiveDao;
	private ArchivedEntryDao archivedEntryDao;
	
	private ConversionService conversionService;
	
	private EntityFactory entityFactory;
	
	private Archive getArchive(User user, Long id) {
		Archive archive = archiveDao.find(user, id);
		if (archive == null) {
			throw new ServiceException("Archive not found, ID " + id, ServiceStatus.RESOURCE_NOT_FOUND);
		}
		return archive;
	}
	
	@Override
	public ArchiveDto createArchive(String username, String title) {
		User user = this.getUser(username);
		if (archiveDao.find(user, title) != null) {
			throw new ServiceException("Archive already exists.", ServiceStatus.RESOURCE_ALREADY_EXISTS);
		}
		Archive archive = archiveDao.save(entityFactory.createArchive(user, title, archiveDao.getMaxOrder(user) + 1));
		return conversionService.convert(archive, ArchiveDto.class);
	}

	@Override
	public void deleteArchive(String username, Long archiveId) {
		User user = this.getUser(username);
		Archive archive = this.getArchive(user, archiveId);
		archiveDao.delete(archive);
	}

	@Override
	public void moveUp(String username, Long archiveId) {
		User user = this.getUser(username);
		
		List<Archive> archives = archiveDao.list(user);
		Integer index = null;
		for (int i = 0; i < archives.size(); ++i) {
			if (archives.get(i).getId().equals(archiveId)) {
				index = i;
			}
		}
		
		if (index == null || index == 0) {
			return;
		}
		
		swap(archives.get(index - 1), archives.get(index));
	}

	@Override
	public void moveDown(String username, Long archiveId) {
		User user = this.getUser(username);
		
		List<Archive> archives = archiveDao.list(user);
		Integer index = null;
		for (int i = 0; i < archives.size(); ++i) {
			if (archives.get(i).getId().equals(archiveId)) {
				index = i;
			}
		}
		
		if (index == null || index == archives.size() - 1) {
			return;
		}
		
		swap(archives.get(index), archives.get(index + 1));
	}
	
	private void swap(Archive archive1, Archive archive2) {
		int order = archive1.getOrder();
		archive1.setOrder(archive2.getOrder());
		archive2.setOrder(order);
		
		List<Archive> updatedArchives = new ArrayList<Archive>();
		updatedArchives.add(archive1);
		updatedArchives.add(archive2);
		
		archiveDao.saveAll(updatedArchives);
	}

	@Override
	public List<ArchiveDto> list(String username) {
		User user = this.getUser(username);
		List<ArchiveDto> dtos = new ArrayList<ArchiveDto>();
		for (Archive archive : archiveDao.list(user)) {
			dtos.add(conversionService.convert(archive, ArchiveDto.class));
		}
		return dtos;
	}

	@Override
	public void entitle(String username, Long archiveId, String title) {
		User user = this.getUser(username);
		Archive archive = this.getArchive(user, archiveId);
		
		archive.setTitle(title);
		archiveDao.save(archive);
	}
	
	@Override
	public void archive(String username, Long groupId, Long subscriptionId,
			Long entryId, Long archiveId) {
		User user = this.getUser(username);
		SubscriptionGroup group = this.getGroup(user, groupId);
		Subscription subscription = this.getSubscription(group, subscriptionId);
		
		FeedEntry feedEntry = feedEntryDao.find(subscription, entryId);
		Archive archive = this.getArchive(user, archiveId);
		
		ArchivedEntry entity = conversionService.convert(feedEntry, ArchivedEntry.class);
		entity.setArchive(archive);
		archivedEntryDao.save(entity);
	}
	
	@Override
	public List<ArchivedEntryDto> listEntries(ArchivedEntryFilterData filterData) {
		User user = this.getUser(filterData.getUsername());
		List<ArchivedEntry> entries;
		if (filterData.getArchiveId() == null) {
			entries = archivedEntryDao.list(user, filterData);
		} else {
			Archive archive = this.getArchive(user, filterData.getArchiveId());
			entries = archivedEntryDao.list(archive, filterData);
		}
		List<ArchivedEntryDto> dtos = new ArrayList<ArchivedEntryDto>();
		for (ArchivedEntry entry : entries) {
			dtos.add(conversionService.convert(entry, ArchivedEntryDto.class));
		}
		return dtos;
	}

	public FeedEntryDao getFeedEntryDao() {
		return feedEntryDao;
	}

	public void setFeedEntryDao(FeedEntryDao feedEntryDao) {
		this.feedEntryDao = feedEntryDao;
	}

	public ArchiveDao getArchiveDao() {
		return archiveDao;
	}

	public void setArchiveDao(ArchiveDao archiveDao) {
		this.archiveDao = archiveDao;
	}

	public ArchivedEntryDao getArchivedEntryDao() {
		return archivedEntryDao;
	}

	public void setArchivedEntryDao(ArchivedEntryDao archivedEntryDao) {
		this.archivedEntryDao = archivedEntryDao;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public EntityFactory getEntityFactory() {
		return entityFactory;
	}

	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

}
