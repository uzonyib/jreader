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

import jreader.dao.FeedEntryFilter.Selection;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;
import jreader.services.GroupService;
import jreader.web.controller.ResponseEntity;
import jreader.web.controller.util.SelectionEditor;

@RestController
@RequestMapping(value = "/reader")
public class EntryController {

    private final GroupService groupService;
    private final FeedEntryService feedEntryService;
    
    public EntryController(final GroupService groupService, final FeedEntryService feedEntryService) {
        this.groupService = groupService;
        this.feedEntryService = feedEntryService;
    }

    @InitBinder
    public void initBinder(final WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(Selection.class, new SelectionEditor());
    }

    @RequestMapping(value = "/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Selection selection, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Long groupId, @PathVariable final Selection selection,
            @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), groupId, selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final Selection selection, @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity(
                feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), groupId, subscriptionId, selection, ascending, offset, count)));
    }

    @RequestMapping(value = "/entries", method = RequestMethod.POST)
    public ResponseEntity readAll(final Principal principal, @RequestBody final Map<Long, Map<Long, List<Long>>> ids) {
        feedEntryService.markRead(principal.getName(), ids);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{id}/starred", method = RequestMethod.PUT)
    public ResponseEntity setStarred(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final Long id, @RequestParam final boolean value) {
        if (value) {
            feedEntryService.star(principal.getName(), groupId, subscriptionId, id);
        } else {
            feedEntryService.unstar(principal.getName(), groupId, subscriptionId, id);
        }
        return new ResponseEntity();
    }

}
