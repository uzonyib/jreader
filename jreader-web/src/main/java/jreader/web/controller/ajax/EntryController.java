package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jreader.dao.FeedEntryFilter.Selection;
import jreader.dto.FeedEntryDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;
import jreader.services.SubscriptionService;
import jreader.web.controller.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader")
public class EntryController {

    private final SubscriptionService subscriptionService;
    private final FeedEntryService feedEntryService;

    public EntryController(final SubscriptionService subscriptionService, final FeedEntryService feedEntryService) {
        this.subscriptionService = subscriptionService;
        this.feedEntryService = feedEntryService;
    }

    @RequestMapping(value = "/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity<List<FeedEntryDto>> list(final Principal principal, @PathVariable final String selection, @RequestParam final int offset,
            @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity<List<FeedEntryDto>>(
                feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), parseSelection(selection), ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity<List<FeedEntryDto>> list(final Principal principal, @PathVariable final Long groupId, @PathVariable final String selection,
            @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity<List<FeedEntryDto>>(
                feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), groupId, parseSelection(selection), ascending, offset, count)));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{selection}", method = RequestMethod.GET)
    public ResponseEntity<List<FeedEntryDto>> list(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final String selection, @RequestParam final int offset, @RequestParam final int count, @RequestParam final boolean ascending) {
        return new ResponseEntity<List<FeedEntryDto>>(feedEntryService.listEntries(
                new FeedEntryFilterData(principal.getName(), groupId, subscriptionId, parseSelection(selection), ascending, offset, count)));
    }

    @RequestMapping(value = "/entries", method = RequestMethod.POST)
    public ResponseEntity<List<SubscriptionGroupDto>> readAll(final Principal principal, @RequestBody final Map<Long, Map<Long, List<Long>>> ids) {
        feedEntryService.markRead(principal.getName(), ids);
        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

    @RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{id}/starred", method = RequestMethod.PUT)
    public ResponseEntity<Void> setStarred(final Principal principal, @PathVariable final Long groupId, @PathVariable final Long subscriptionId,
            @PathVariable final Long id, @RequestParam final boolean value) {
        if (value) {
            feedEntryService.star(principal.getName(), groupId, subscriptionId, id);
        } else {
            feedEntryService.unstar(principal.getName(), groupId, subscriptionId, id);
        }
        return new ResponseEntity<Void>();
    }

    private Selection parseSelection(final String selection) {
        try {
            return Selection.valueOf(Selection.class, selection.toUpperCase(Locale.ENGLISH));
        } catch (final Exception e) {
            return Selection.ALL;
        }
    }

}
