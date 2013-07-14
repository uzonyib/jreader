package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jreader.service.FeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

@Controller
@RequestMapping(value = "/cron")
public class CronJobController {
	
	private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
	
	@Autowired
	private FeedService feedService;
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public void refreshFeeds(HttpServletRequest request, HttpServletResponse response, Principal principal) throws JsonIOException, IOException {
		Map<String, String> result = new HashMap<String, String>();
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			feedService.refreshFeeds();
			result.put("errorCode", "0");
			LOG.info("Feeds refreshed.");
		} else {
			result.put("errorCode", "1");
			LOG.warning("Feed refresh prevented.");
		}
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}

}
