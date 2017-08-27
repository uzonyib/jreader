package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.ArchiveService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping("/reader/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping
    public ResponseEntity listAll(final Principal principal) {
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @PostMapping
    public ResponseEntity create(final Principal principal, @RequestParam final String title) {
        archiveService.createArchive(principal.getName(), title);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(final Principal principal, @PathVariable final Long id) {
        archiveService.deleteArchive(principal.getName(), id);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @PutMapping("/{id}/title")
    public ResponseEntity entitle(final Principal principal, @PathVariable final Long id, @RequestParam final String value) {
        archiveService.entitle(principal.getName(), id, value);
        return new ResponseEntity(archiveService.list(principal.getName()));
    }

    @PutMapping("/{id}/order")
    public ResponseEntity move(final Principal principal, @PathVariable final Long id, @RequestParam final boolean up) {
        if (up) {
            archiveService.moveUp(principal.getName(), id);
        } else {
            archiveService.moveDown(principal.getName(), id);
        }

        return new ResponseEntity(archiveService.list(principal.getName()));
    }

}
