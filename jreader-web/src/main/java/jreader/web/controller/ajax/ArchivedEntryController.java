package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import jreader.dto.ArchivedEntryDto;
import jreader.dto.StatusDto;
import jreader.services.ArchiveService;
import jreader.services.ArchivedEntryFilterData;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader/archives")
public class ArchivedEntryController {

    private ArchiveService archiveService;

    public ArchivedEntryController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RequestMapping(value = "/{archiveId}/entries", method = RequestMethod.POST)
    public StatusDto archive(Principal principal, @PathVariable Long archiveId, @RequestParam Long groupId, @RequestParam Long subscriptionId,
            @RequestParam Long entryId) {
        archiveService.archive(principal.getName(), groupId, subscriptionId, entryId, archiveId);
        return new StatusDto(0);
    }

    @RequestMapping(value = "/entries", method = RequestMethod.GET)
    public List<ArchivedEntryDto> list(Principal principal, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending) {
        return archiveService.listEntries(new ArchivedEntryFilterData(principal.getName(), ascending, offset, count));
    }

    @RequestMapping(value = "/{archiveId}/entries", method = RequestMethod.GET)
    public List<ArchivedEntryDto> list(Principal principal, @PathVariable Long archiveId, @RequestParam int offset, @RequestParam int count,
            @RequestParam boolean ascending) {
        return archiveService.listEntries(new ArchivedEntryFilterData(principal.getName(), archiveId, ascending, offset, count));
    }

    @RequestMapping(value = "/{archiveId}/entries/{entryId}", method = RequestMethod.DELETE)
    public StatusDto delete(Principal principal, @PathVariable Long archiveId, @PathVariable Long entryId) {
        archiveService.deleteEntry(principal.getName(), archiveId, entryId);
        return new StatusDto(0);
    }

}
