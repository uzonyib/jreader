package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.dao.PostFilter.PostType;
import jreader.services.PostFilter;
import jreader.services.PostService;
import jreader.services.GroupService;
import jreader.web.controller.ResponseEntity;
import jreader.web.controller.util.SelectionEditor;

@RestController
@RequestMapping(value = "/reader")
public class PostController {

    private final GroupService groupService;
    private final PostService postService;
    
    public PostController(final GroupService groupService, final PostService postService) {
        this.groupService = groupService;
        this.postService = postService;
    }

    @InitBinder
    public void initBinder(final WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(PostType.class, new SelectionEditor());
    }

    @RequestMapping(value = "/posts/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final PostType selection, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(postService.list(new PostFilter(principal.getName(), selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/posts/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Long groupId, @PathVariable final PostType selection,
            @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(postService.list(new PostFilter(principal.getName(), groupId, selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/posts/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final PostType selection, @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(
                postService.list(new PostFilter(principal.getName(), groupId, subscriptionId, selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public ResponseEntity readAll(final Principal principal, @RequestBody final Map<Long, Map<Long, List<Long>>> ids) {
        postService.markRead(principal.getName(), ids);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/posts/{id}/bookmarked", method = RequestMethod.PUT)
    public ResponseEntity setBookmarked(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final Long id, @RequestParam final boolean value) {
        if (value) {
            postService.bookmark(principal.getName(), groupId, subscriptionId, id);
        } else {
            postService.deleteBookmark(principal.getName(), groupId, subscriptionId, id);
        }
        return new ResponseEntity();
    }

}
