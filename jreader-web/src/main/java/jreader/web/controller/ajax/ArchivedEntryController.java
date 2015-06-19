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

    private final ArchiveService archiveService;

    public ArchivedEntryController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RequestMapping(value = "/{archiveId}/entries", method = RequestMethod.POST)
    public StatusDto archive(final Principal principal, @PathVariable final Long archiveId, @RequestParam final Long groupId,
            @RequestParam final Long subscriptionId, @RequestParam final Long entryId) {
        archiveService.archive(principal.getName(), groupId, subscriptionId, entryId, archiveId);
        return new StatusDto(0);
    }

    @RequestMapping(value = "/entries", method = RequestMethod.GET)
    public List<ArchivedEntryDto> list(final Principal principal, @RequestParam final int offset, @RequestParam final int count,
            @RequestParam final boolean ascending) {
        return archiveService.listEntries(new ArchivedEntryFilterData(principal.getName(), ascending, offset, count));
    }

    @RequestMapping(value = "/{archiveId}/entries", method = RequestMethod.GET)
    public List<ArchivedEntryDto> list(final Principal principal, @PathVariable final Long archiveId, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return archiveService.listEntries(new ArchivedEntryFilterData(principal.getName(), archiveId, ascending, offset, count));
    }

    @RequestMapping(value = "/{archiveId}/entries/{entryId}", method = RequestMethod.DELETE)
    public StatusDto delete(final Principal principal, @PathVariable final Long archiveId, @PathVariable final Long entryId) {
        archiveService.deleteEntry(principal.getName(), archiveId, entryId);
        return new StatusDto(0);
    }

}
