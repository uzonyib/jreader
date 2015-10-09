package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.ArchiveService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/reader/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listAll(final Principal principal) {
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(final Principal principal, @RequestParam final String title) {
        archiveService.createArchive(principal.getName(), title);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(final Principal principal, @PathVariable final Long id) {
        archiveService.deleteArchive(principal.getName(), id);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @RequestMapping(value = "/{id}/title", method = RequestMethod.PUT)
    public ResponseEntity entitle(final Principal principal, @PathVariable final Long id, @RequestParam final String value) {
        archiveService.entitle(principal.getName(), id, value);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @RequestMapping(value = "/{id}/order", method = RequestMethod.PUT)
    public ResponseEntity move(final Principal principal, @PathVariable final Long id, @RequestParam final boolean up) {
        if (up) {
            archiveService.moveUp(principal.getName(), id);
        } else {
            archiveService.moveDown(principal.getName(), id);
        }

        return new ResponseEntity(archiveService.list(principal.getName()));
    }

}
