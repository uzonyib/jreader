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

    private final ArchiveService archiveService;

    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ArchiveDto> getArchives(final Principal principal) {
        return archiveService.list(principal.getName());
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<ArchiveDto> create(final Principal principal, @RequestParam final String title) {
        archiveService.createArchive(principal.getName(), title);
        return archiveService.list(principal.getName());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public List<ArchiveDto> delete(final Principal principal, @PathVariable final Long id) {
        archiveService.deleteArchive(principal.getName(), id);
        return archiveService.list(principal.getName());
    }

    @RequestMapping(value = "/{id}/title", method = RequestMethod.PUT)
    public List<ArchiveDto> entitle(final Principal principal, @PathVariable final Long id, @RequestParam final String value) {
        if (value != null && !"".equals(value)) {
            archiveService.entitle(principal.getName(), id, value);
        }

        return archiveService.list(principal.getName());
    }

    @RequestMapping(value = "/{id}/order", method = RequestMethod.PUT)
    public List<ArchiveDto> move(final Principal principal, @PathVariable final Long id, @RequestParam final boolean up) {
        if (up) {
            archiveService.moveUp(principal.getName(), id);
        } else {
            archiveService.moveDown(principal.getName(), id);
        }

        return archiveService.list(principal.getName());
    }

}
