package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jreader.dto.StatusDto;
import jreader.service.FeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

@Controller
@RequestMapping(value = "/cron")
public class CronJobController {
	
	private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
	
	@Value("#{cronProps.minAgeToDelete}")
	private int minAgeToDelete;
	
	@Value("#{cronProps.minCountToKeep}")
	private int minCountToKeep;
	
	@Autowired
	private FeedService feedService;
	
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public void refreshFeeds(HttpServletRequest request, HttpServletResponse response, Principal principal) throws JsonIOException, IOException {
		StatusDto result = new StatusDto();
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			feedService.refreshFeeds();
			result.setErrorCode(0);
			LOG.info("Feeds refreshed.");
		} else {
			result.setErrorCode(1);
			LOG.warning("Feed refresh prevented.");
		}
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}

	@RequestMapping(value = "/cleanup", method = RequestMethod.GET)
	public void cleanup(HttpServletRequest request, HttpServletResponse response, Principal principal) throws JsonIOException, IOException {
		StatusDto result = new StatusDto();
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			feedService.cleanup(minAgeToDelete, minCountToKeep);
			result.setErrorCode(0);
			LOG.info("Cleanup completed.");
		} else {
			result.setErrorCode(1);
			LOG.warning("Cleanup prevented.");
		}
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}

}
