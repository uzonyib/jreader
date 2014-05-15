package jreader.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import jreader.dao.FeedEntryFilter.Selection;
import jreader.dto.FeedEntryDto;
import jreader.dto.StatusDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;
import jreader.services.ServiceException;
import jreader.services.SubscriptionService;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader")
public class AjaxController {
	
	private static final Logger LOG = Logger.getLogger(AjaxController.class.getName());
	
	private SubscriptionService subscriptionService;
	private FeedEntryService feedEntryService;
	
	@RequestMapping(value = "/create-group", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> createGroup(@RequestParam("title") String title, Principal principal) {
		subscriptionService.createGroup(principal.getName(), title);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> subscribe(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("url") String url, Principal principal) {
		subscriptionService.subscribe(principal.getName(), subscriptionGroupId, url);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> unsubscribe(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.unsubscribe(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> delete(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.deleteGroup(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-group-up", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> moveUp(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-group-down", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> moveDown(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-up", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> moveUp(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-down", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> moveDown(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public List<SubscriptionGroupDto> getSubscriptions(Principal principal) {
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/entries/all/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(@PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending, Principal principal) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), parseSelection(selection), ascending, offset, count));
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(@PathVariable Long subscriptionGroupId, @PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending, Principal principal) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, parseSelection(selection), ascending, offset, count));
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/subscription/{subscriptionId}/{selection}", method = RequestMethod.GET)
	public List<FeedEntryDto> getEntries(@PathVariable Long subscriptionGroupId, @PathVariable Long subscriptionId, @PathVariable String selection, @RequestParam int offset, @RequestParam int count, @RequestParam boolean ascending, Principal principal) {
		return feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, subscriptionId, parseSelection(selection), ascending, offset, count));
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> entitle(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, Principal principal) {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, subscriptionId, title);
		}
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/entitle-group", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> entitle(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, Principal principal) {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, title);
		}
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public List<SubscriptionGroupDto> read(@RequestParam("subscriptionGroupIds") String subscriptionGroupIds, @RequestParam("subscriptionIds") String subscriptionIds, @RequestParam("ids") String ids, Principal principal) {
		feedEntryService.markRead(principal.getName(), parseIds(subscriptionGroupIds), parseIds(subscriptionIds), parseIds(ids));
		return getSubscriptions(principal);
	}
	
	private List<Long> parseIds(String rawIds) {
		String[] splittedIds = rawIds.split(",");
		List<Long> ids = new ArrayList<Long>();
		for (String rawId : splittedIds) {
			ids.add(Long.valueOf(rawId));
		}
		return ids;
	}

	@RequestMapping(value = "/star", method = RequestMethod.POST)
	public StatusDto star(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, Principal principal) {
		feedEntryService.star(principal.getName(), subscriptionGroupId, subscriptionId, id);
		return new StatusDto(0);
	}
	
	@RequestMapping(value = "/unstar", method = RequestMethod.POST)
	public StatusDto unstar(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, Principal principal) {
		feedEntryService.unstar(principal.getName(), subscriptionGroupId, subscriptionId, id);
		return new StatusDto(0);
	}

	@ExceptionHandler(ServiceException.class)
	public StatusDto handleServiceException(ServiceException e, HttpServletResponse response) {
		response.setStatus(e.getStatus().getCode());
		return new StatusDto(1, e.getMessage());
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
