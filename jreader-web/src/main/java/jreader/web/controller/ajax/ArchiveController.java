package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.ArchiveDto;
import jreader.services.ArchiveService;

@RestController
@RequestMapping("/reader/archives")
public class ArchiveController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @GetMapping
    public List<ArchiveDto> listAll(final Principal principal) {
        return archiveService.list(principal.getName());
    }

    @PostMapping
    public List<ArchiveDto> create(final Principal principal, @RequestParam final String title) {
        archiveService.createArchive(principal.getName(), title);
        return archiveService.list(principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public List<ArchiveDto> delete(final Principal principal, @PathVariable final Long id) {
        archiveService.deleteArchive(principal.getName(), id);
        return archiveService.list(principal.getName());
    }

    @PutMapping("/{id}/title")
    public List<ArchiveDto> entitle(final Principal principal, @PathVariable final Long id, @RequestParam final String value) {
        archiveService.entitle(principal.getName(), id, value);
        return archiveService.list(principal.getName());
    }

    @PutMapping("/{id}/order")
    public List<ArchiveDto> move(final Principal principal, @PathVariable final Long id, @RequestParam final boolean up) {
        if (up) {
            archiveService.moveUp(principal.getName(), id);
        } else {
            archiveService.moveDown(principal.getName(), id);
        }

        return archiveService.list(principal.getName());
    }

}
