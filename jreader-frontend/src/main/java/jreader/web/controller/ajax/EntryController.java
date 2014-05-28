package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import jreader.dao.FeedEntryFilter.Selection;
import jreader.dto.FeedEntryDto;
import jreader.dto.StatusDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;
import jreader.services.SubscriptionService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader")
public class EntryController {
	
	private SubscriptionService subscriptionService;
	private FeedEntryService feedEntryService;
	
	@RequestMapping(value = "/entries/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(Principal principal, @PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), parseSelection(selection), ascending, offset, count));
	}
	
	@RequestMapping(value = "/groups/{groupId}/entries/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(Principal principal, @PathVariable Long groupId, @PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), groupId, parseSelection(selection), ascending, offset, count));
	}
	
	@RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(Principal principal, @PathVariable Long groupId, @PathVariable Long subscriptionId, @PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), groupId, subscriptionId, parseSelection(selection), ascending, offset, count));
	}

	@RequestMapping(value = "/entries", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> readAll(Principal principal, @RequestBody Map<Long, Map<Long, List<Long>>> ids) {
		feedEntryService.markRead(principal.getName(), ids);
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/groups/{groupId}/subscriptions/{subscriptionId}/entries/{id}/starred", method = RequestMethod.PUT)
	public StatusDto setStarred(Principal principal, @PathVariable Long groupId, @PathVariable Long subscriptionId, @PathVariable Long id, @RequestParam boolean value) {
		if (value) {
			feedEntryService.star(principal.getName(), groupId, subscriptionId, id);
		} else {
			feedEntryService.unstar(principal.getName(), groupId, subscriptionId, id);
		}
		return new StatusDto(0);
	}

	private Selection parseSelection(String selection) {
		try {
			return Selection.valueOf(Selection.class, selection.toUpperCase());
		} catch (Exception e) {
			return Selection.ALL;
		}
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public FeedEntryService getFeedEntryService() {
		return feedEntryService;
	}

	public void setFeedEntryService(FeedEntryService feedEntryService) {
		this.feedEntryService = feedEntryService;
	}
	
}
