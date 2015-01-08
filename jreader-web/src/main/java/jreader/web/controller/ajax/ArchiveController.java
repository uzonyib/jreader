package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import jreader.dto.ArchiveDto;
import jreader.services.ArchiveService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader/archives")
public class ArchiveController {
	
	private ArchiveService archiveService;
	
	public ArchiveController(ArchiveService archiveService) {
		this.archiveService = archiveService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<ArchiveDto> getArchives(Principal principal) {
		return archiveService.list(principal.getName());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public List<ArchiveDto> create(Principal principal, @RequestParam String title) {
		archiveService.createArchive(principal.getName(), title);
		return archiveService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public List<ArchiveDto> delete(Principal principal, @PathVariable Long id) {
		archiveService.deleteArchive(principal.getName(), id);
		return archiveService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{id}/title", method = RequestMethod.PUT)
	public List<ArchiveDto> entitle(Principal principal, @PathVariable Long id, @RequestParam String value) {
		if (value != null && !"".equals(value)) {
			archiveService.entitle(principal.getName(), id, value);
		}
		
		return archiveService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{id}/order", method = RequestMethod.PUT)
	public List<ArchiveDto> move(Principal principal, @PathVariable Long id, @RequestParam boolean up) {
		if (up) {
			archiveService.moveUp(principal.getName(), id);
		} else {
			archiveService.moveDown(principal.getName(), id);
		}
		
		return archiveService.list(principal.getName());
	}

}
