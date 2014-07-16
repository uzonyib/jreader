package jreader.web.controller.ajax;

import java.security.Principal;

import jreader.dto.StatusDto;
import jreader.services.ArchiveService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader/archives/{archiveId}")
public class ArchivedEntryController {
	
	private ArchiveService archiveService;
	
	@RequestMapping(value = "/entries", method = RequestMethod.POST)
	public StatusDto archive(Principal principal, @PathVariable Long archiveId, @RequestParam Long groupId, @RequestParam Long subscriptionId, @RequestParam Long entryId) {
		archiveService.archive(principal.getName(), groupId, subscriptionId, entryId, archiveId);
		return new StatusDto(0);
	}

	public ArchiveService getArchiveService() {
		return archiveService;
	}

	public void setArchiveService(ArchiveService archiveService) {
		this.archiveService = archiveService;
	}
	
}
