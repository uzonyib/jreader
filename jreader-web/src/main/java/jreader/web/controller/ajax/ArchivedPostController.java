package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.ArchivedPostDto;
import jreader.services.ArchiveService;
import jreader.services.ArchivedPostFilter;

@RestController
@RequestMapping("/reader/archives")
public class ArchivedPostController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchivedPostController(final ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping("/{archiveId}/posts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(final Principal principal, @PathVariable final Long archiveId, @RequestParam final Long groupId,
            @RequestParam final Long subscriptionId, @RequestParam final Long postId) {
        archiveService.archive(principal.getName(), groupId, subscriptionId, postId, archiveId);
    }

    @GetMapping("/posts")
    public List<ArchivedPostDto> list(final Principal principal, final Pageable page) {
        return archiveService.listPosts(new ArchivedPostFilter(principal.getName(), page));
    }

    @GetMapping("/{archiveId}/posts")
    public List<ArchivedPostDto> list(final Principal principal, @PathVariable final Long archiveId, final Pageable page) {
        return archiveService.listPosts(new ArchivedPostFilter(principal.getName(), archiveId, page));
    }

    @DeleteMapping("/{archiveId}/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final Principal principal, @PathVariable final Long archiveId, @PathVariable final Long postId) {
        archiveService.deletePost(principal.getName(), archiveId, postId);
    }

}
