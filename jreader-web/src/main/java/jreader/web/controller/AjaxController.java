package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jreader.dto.FeedDto;
import jreader.dto.FeedEntryDto;
import jreader.service.FeedService;
import jreader.service.SubscriptionService;

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
		List<FeedDto> feeds = feedService.list(principal.getName());
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

	@RequestMapping(value = "/entries/{id}", method = RequestMethod.GET)
	public void getEntries(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		List<FeedEntryDto> feeds = feedService.listEntries(id);
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		gson.toJson(feeds, response.getWriter());
	}

}
