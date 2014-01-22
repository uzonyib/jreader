package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/reader")
public class AjaxController {
	
	private SubscriptionService subscriptionService;
	private FeedEntryService feedEntryService;
	
	private int pageSize;
	
	@RequestMapping(value = "/create-group", method = RequestMethod.POST)
	public void createGroup(@RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.createGroup(principal.getName(), title);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public void subscribe(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("url") String url, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.subscribe(principal.getName(), subscriptionGroupId, url);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public void unsubscribe(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.unsubscribe(principal.getName(), subscriptionGroupId, subscriptionId);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.deleteGroup(principal.getName(), subscriptionGroupId);
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/move-group-up", method = RequestMethod.POST)
	public void moveUp(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId);
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/move-group-down", method = RequestMethod.POST)
	public void moveDown(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId);
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/move-up", method = RequestMethod.POST)
	public void moveUp(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.moveUp(principal.getName(), subscriptionGroupId, subscriptionId);
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/move-down", method = RequestMethod.POST)
	public void moveDown(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.moveDown(principal.getName(), subscriptionGroupId, subscriptionId);
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public void getSubscriptions(HttpServletResponse response, Principal principal) throws IOException {
		List<SubscriptionGroupDto> feeds = subscriptionService.list(principal.getName());
		writeResponse(response, feeds);
	}
	
	@RequestMapping(value = "/entries/all/{selection}/{pageIndex}", method = RequestMethod.GET)
	public void getEntries(@PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		writeResponse(response, feeds);
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/{selection}/{pageIndex}", method = RequestMethod.GET)
	public void getEntries(@PathVariable Long subscriptionGroupId, @PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		writeResponse(response, feeds);
	}
	
	@RequestMapping(value = "/entries/group/{subscriptionGroupId}/subscription/{subscriptionId}/{selection}/{pageIndex}", method = RequestMethod.GET)
	public void getEntries(@PathVariable Long subscriptionGroupId, @PathVariable Long subscriptionId, @PathVariable String selection, @PathVariable int pageIndex, @RequestParam boolean ascending, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedEntryService.listEntries(new FeedEntryFilterData(principal.getName(), subscriptionGroupId, subscriptionId, parseSelection(selection), ascending, getOffset(pageIndex), pageSize));
		writeResponse(response, feeds);
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST)
	public void entitle(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, subscriptionId, title);
		}
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/entitle-group", method = RequestMethod.POST)
	public void entitle(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, title);
		}
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public void read(@RequestParam("subscriptionGroupIds") String subscriptionGroupIds, @RequestParam("subscriptionIds") String subscriptionIds, @RequestParam("ids") String ids, HttpServletResponse response, Principal principal) throws IOException {
		feedEntryService.markRead(principal.getName(), parseIds(subscriptionGroupIds), parseIds(subscriptionIds), parseIds(ids));
		getSubscriptions(response, principal);
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
	public void star(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		feedEntryService.star(principal.getName(), subscriptionGroupId, subscriptionId, id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		writeResponse(response, result);
	}
	
	@RequestMapping(value = "/unstar", method = RequestMethod.POST)
	public void unstar(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		feedEntryService.unstar(principal.getName(), subscriptionGroupId, subscriptionId, id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		writeResponse(response, result);
	}
	
	private void writeResponse(HttpServletResponse response, Object data) throws IOException {
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(data, response.getWriter());
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
