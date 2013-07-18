package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jreader.dto.FeedEntryDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.service.FeedEntryService;
import jreader.service.FeedService;
import jreader.service.SubscriptionService;
import jreader.web.dto.StatusDto;

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
	private FeedEntryService actionService;

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public void subscribe(@RequestParam("url") String url, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.subscribe(principal.getName(), url);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public void unsubscribe(@RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.unsubscribe(principal.getName(), id);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public void getSubscriptions(HttpServletResponse response, Principal principal) throws IOException {
		List<SubscriptionGroupDto> feeds = subscriptionService.list(principal.getName());
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

	@RequestMapping(value = "/entries", method = RequestMethod.GET)
	public void getEntries(@RequestParam("ids") String rawIds, @RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listEntries(principal.getName(), parseIds(rawIds), onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}
	
	@RequestMapping(value = "/assign", method = RequestMethod.POST)
	public void assign(@RequestParam("id") Long id, @RequestParam("group") String group, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.assign(principal.getName(), id, "".equals(group) ? null : group);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST)
	public void entitle(@RequestParam("id") Long id, @RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), id, title);
		}
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public void read(@RequestParam("ids") String rawIds, HttpServletResponse response, Principal principal) throws IOException {
		actionService.markRead(principal.getName(), parseIds(rawIds));
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
	public void star(@RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		actionService.star(principal.getName(), id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}
	
	@RequestMapping(value = "/unstar", method = RequestMethod.POST)
	public void unstar(@RequestParam("id") Long id, HttpServletResponse response, Principal principal) throws IOException {
		actionService.unstar(principal.getName(), id);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}
	
	@RequestMapping(value = "/starred", method = RequestMethod.GET)
	public void getStarredEntries(@RequestParam("only-unread") boolean onlyUnread, @RequestParam("reverse-order") boolean reverseOrder, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listStarredEntries(principal.getName(), onlyUnread, reverseOrder);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

}
