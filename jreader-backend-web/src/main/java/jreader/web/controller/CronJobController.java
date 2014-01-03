package jreader.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jreader.dto.StatusDto;
import jreader.service.CronService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

@Controller
@RequestMapping(value = "/cron")
public class CronJobController {
	
	private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
	
	private int minAgeToDelete;
	private int minCountToKeep;
	
	private CronService cronService;

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public void refreshFeeds(HttpServletRequest request, HttpServletResponse response, Principal principal) throws JsonIOException, IOException {
		StatusDto result = new StatusDto();
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			cronService.refreshFeeds();
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
			cronService.cleanup(minAgeToDelete, minCountToKeep);
			result.setErrorCode(0);
			LOG.info("Cleanup completed.");
		} else {
			result.setErrorCode(1);
			LOG.warning("Cleanup prevented.");
		}
		Gson gson = new Gson();
		gson.toJson(result, response.getWriter());
	}

	public int getMinAgeToDelete() {
		return minAgeToDelete;
	}

	public void setMinAgeToDelete(int minAgeToDelete) {
		this.minAgeToDelete = minAgeToDelete;
	}

	public int getMinCountToKeep() {
		return minCountToKeep;
	}

	public void setMinCountToKeep(int minCountToKeep) {
		this.minCountToKeep = minCountToKeep;
	}

	public CronService getCronService() {
		return cronService;
	}

	public void setCronService(CronService cronService) {
		this.cronService = cronService;
	}

}
