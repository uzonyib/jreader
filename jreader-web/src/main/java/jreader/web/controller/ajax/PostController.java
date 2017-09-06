package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jreader.dao.PostFilter.PostType;
import jreader.dto.GroupDto;
import jreader.dto.PostDto;
import jreader.services.GroupService;
import jreader.services.PostFilter;
import jreader.services.PostService;
import jreader.web.controller.util.SelectionEditor;

@RestController
@RequestMapping("/reader")
public class PostController {

    private final GroupService groupService;
    private final PostService postService;

    @Autowired
    public PostController(final GroupService groupService, final PostService postService) {
        this.groupService = groupService;
        this.postService = postService;
    }

    @InitBinder
    public void initBinder(final WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(PostType.class, new SelectionEditor());
    }

    @GetMapping("/posts/{selection}")
    public List<PostDto> list(final Principal principal, @PathVariable final PostType selection, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return postService.list(new PostFilter(principal.getName(), selection, ascending, offset, count));
    }

    @GetMapping("/groups/{groupId}/posts/{selection}")
    public List<PostDto> list(final Principal principal, @PathVariable final Long groupId, @PathVariable final PostType selection,
            @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return postService.list(new PostFilter(principal.getName(), groupId, selection, ascending, offset, count));
    }

    @GetMapping("/groups/{groupId}/subscriptions/{subscriptionId}/posts/{selection}")
    public List<PostDto> list(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final PostType selection, @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return postService.list(new PostFilter(principal.getName(), groupId, subscriptionId, selection, ascending, offset, count));
    }

    @PostMapping("/posts")
    public List<GroupDto> readAll(final Principal principal, @RequestBody final Map<Long, Map<Long, List<Long>>> ids) {
        postService.markRead(principal.getName(), ids);
        return groupService.list(principal.getName());
    }

    @PutMapping("/groups/{groupId}/subscriptions/{subscriptionId}/posts/{id}/bookmarked")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setBookmarked(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final Long id, @RequestParam final boolean value) {
        if (value) {
            postService.bookmark(principal.getName(), groupId, subscriptionId, id);
        } else {
            postService.deleteBookmark(principal.getName(), groupId, subscriptionId, id);
        }
    }

}
