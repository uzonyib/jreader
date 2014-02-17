package jreader.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedEntryFilter.Selection;
import jreader.dto.FeedEntryDto;
import jreader.dto.StatusDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;
import jreader.services.SubscriptionService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/reader")
public class AjaxController {
	
	private SubscriptionService subscriptionService;
	private FeedEntryService feedEntryService;
	
	private int pageSize;
	
	@RequestMapping(value = "/create-group", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String createGroup(@RequestParam("title") String title, Principal principal) {
		subscriptionService.createGroup(principal.getName(), title);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String subscribe(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("url") String url, Principal principal) {
		subscriptionService.subscribe(principal.getName(), subscriptionGroupId, url);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String unsubscribe(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.unsubscribe(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String delete(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.deleteGroup(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-group-up", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String moveUp(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-group-down", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String moveDown(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-up", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String moveUp(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/move-down", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String moveDown(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, Principal principal) {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId, subscriptionId);
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getSubscriptions(Principal principal) {
		List<SubscriptionGroupDto> feeds = subscriptionService.list(principal.getName());
		return new Gson().toJson(feeds);
	}
	
	@RequestMapping(value = "/entries/all/{selection}/{pageIndex}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getEntries(@PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, Principal principal) {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		return new Gson().toJson(feeds);
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/{selection}/{pageIndex}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getEntries(@PathVariable Long subscriptionGroupId, @PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, Principal principal) {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		return new Gson().toJson(feeds);
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/subscription/{subscriptionId}/{selection}/{pageIndex}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getEntries(@PathVariable Long subscriptionGroupId, @PathVariable Long subscriptionId, @PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, Principal principal) {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, subscriptionId, parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		return new Gson().toJson(feeds);
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String entitle(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, Principal principal) {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, subscriptionId, title);
		}
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/entitle-group", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String entitle(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, Principal principal) {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, title);
		}
		return getSubscriptions(principal);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String read(@RequestParam("subscriptionGroupIds") String subscriptionGroupIds, @RequestParam("subscriptionIds") String subscriptionIds, @RequestParam("ids") String ids, Principal principal) {
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

	@RequestMapping(value = "/star", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String star(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, Principal principal) {
		feedEntryService.star(principal.getName(), subscriptionGroupId, subscriptionId, id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		return new Gson().toJson(result);
	}
	
	@RequestMapping(value = "/unstar", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String unstar(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, Principal principal) {
		feedEntryService.unstar(principal.getName(), subscriptionGroupId, subscriptionId, id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		return new Gson().toJson(result);
	}
	
	private Selection parseSelection(String selection) {
		try {
			return Selection.valueOf(Selection.class, selection.toUpperCase());
		} catch (Exception e) {
			return Selection.ALL;
		}
	}
	
	private int getOffset(int pageIndex) {
		return pageIndex * pageSize;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
