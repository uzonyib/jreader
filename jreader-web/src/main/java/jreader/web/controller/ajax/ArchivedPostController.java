package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.ArchiveService;
import jreader.services.ArchivedPostFilter;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/reader/archives")
public class ArchivedPostController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchivedPostController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @RequestMapping(value = "/{archiveId}/posts", method = RequestMethod.POST)
    public ResponseEntity archive(final Principal principal, @PathVariable final Long archiveId, @RequestParam final Long groupId,
            @RequestParam final Long subscriptionId, @RequestParam final Long postId) {
        archiveService.archive(principal.getName(), groupId, subscriptionId, postId, archiveId);
        return new ResponseEntity();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @RequestParam final int offset, @RequestParam final int count,
            @RequestParam final boolean ascending) {
        return new ResponseEntity(archiveService.listPosts(new ArchivedPostFilter(principal.getName(), ascending, offset, count)));
    }

    @RequestMapping(value = "/{archiveId}/posts", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Long archiveId, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(archiveService.listPosts(new ArchivedPostFilter(principal.getName(), archiveId, ascending, offset, count)));
    }

    @RequestMapping(value = "/{archiveId}/posts/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(final Principal principal, @PathVariable final Long archiveId, @PathVariable final Long postId) {
        archiveService.deletePost(principal.getName(), archiveId, postId);
        return new ResponseEntity();
    }

}
