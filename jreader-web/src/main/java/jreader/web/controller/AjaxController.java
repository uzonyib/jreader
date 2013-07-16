package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jreader.dto.FeedEntryDto;
import jreader.dto.SubscriptionDto;
import jreader.service.ActionService;
import jreader.service.FeedService;
import jreader.service.SubscriptionService;
import jreader.web.dto.StatusDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	private ActionService actionService;

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public void subscribe(@RequestParam("url") String url, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.subscribe(principal.getName(), url);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public void unsubscribe(@RequestParam("id") String id, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.unsubscribe(principal.getName(), id);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public void getSubscriptions(HttpServletResponse response, Principal principal) throws IOException {
		List<SubscriptionDto> feeds = subscriptionService.list(principal.getName());
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

	@RequestMapping(value = "/entries/{id}", method = RequestMethod.GET)
	public void getEntries(@PathVariable("id") String id, HttpServletResponse response, Principal principal) throws IOException {
		List<FeedEntryDto> feeds = feedService.listEntries(principal.getName(), id);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}
	
	@RequestMapping(value = "/assign", method = RequestMethod.POST)
	public void assign(@RequestParam("id") String id, @RequestParam("group") String group, HttpServletResponse response, Principal principal) throws IOException {
		subscriptionService.assign(principal.getName(), id, "".equals(group) ? null : group);
		getSubscriptions(response, principal);
	}

	@RequestMapping(value = "/entitle", method = RequestMethod.POST)
	public void entitle(@RequestParam("id") String id, @RequestParam("title") String title, HttpServletResponse response, Principal principal) throws IOException {
		if (title != null && !"".equals(title)) {
			subscriptionService.entitle(principal.getName(), id, title);
		}
		getSubscriptions(response, principal);
	}
	
	@RequestMapping(value = "/read", method = RequestMethod.POST)
	public void read(@RequestParam("feedId") String feedId, @RequestParam("feedEntryId") String feedEntryId, HttpServletResponse response, Principal principal) throws IOException {
		actionService.markRead(principal.getName(), feedId, feedEntryId);
		StatusDto result = new StatusDto();
		result.setErrorCode(0);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}

}
