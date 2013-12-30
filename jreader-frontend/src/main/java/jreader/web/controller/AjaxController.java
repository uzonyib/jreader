package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jreader.dto.FeedEntryDto;
import jreader.dto.StatusDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.service.FeedEntryService;
import jreader.service.FeedService;
import jreader.service.SubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/reader")
public class AjaxController {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private FeedEntryService feedEntryService;
	
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

	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public void getSubscriptions(HttpServletResponse response, Principal principal) throws IOException {
		List<SubscriptionGroupDto> feeds = subscriptionService.list(principal.getName());
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}
	
	@RequestMapping(value = "/entries/all", method = RequestMethod.GET)
	public void getAllEntries(@RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listAllEntries(principal.getName(), onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}
	
	@RequestMapping(value = "/entries/group", method = RequestMethod.GET)
	public void getSubscriptionGroupEntries(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listSubscriptionGroupEntries(principal.getName(), subscriptionGroupId, onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

	@RequestMapping(value = "/entries/subscription", method = RequestMethod.GET)
	public void getSubscriptionEntries(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listSubscriptionEntries(principal.getName(), subscriptionGroupId, subscriptionId, onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST)
	public void entitle(@RequestParam("subscriptionId") Long subscriptionId, @RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), subscriptionGroupId, subscriptionId, title);
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
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}
	
	@RequestMapping(value = "/unstar", method = RequestMethod.POST)
	public void unstar(@RequestParam("subscriptionGroupId") Long subscriptionGroupId, @RequestParam("subscriptionId") Long subscriptionId, @RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		feedEntryService.unstar(principal.getName(), subscriptionGroupId, subscriptionId, id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}
	
	@RequestMapping(value = "/entries/starred", method = RequestMethod.GET)
	public void getStarredEntries(@RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listStarredEntries(principal.getName(), onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

}
