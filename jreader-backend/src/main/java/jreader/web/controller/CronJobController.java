package jreader.web.controller;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jreader.dto.StatusDto;
import jreader.services.CronService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cron")
public class CronJobController {
	
	private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
	
	private int minAgeToDelete;
	private int minCountToKeep;
	
	private CronService cronService;

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public StatusDto refreshFeeds(HttpServletRequest request, HttpServletResponse response, Principal principal) {
		StatusDto result;
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			cronService.refreshFeeds();
			result = new StatusDto(0);
			LOG.info("Feeds refreshed.");
		} else {
			result = new StatusDto(1);
			LOG.warning("Feed refresh prevented.");
		}
		return result;
	}

	@RequestMapping(value = "/cleanup", method = RequestMethod.GET)
	public StatusDto cleanup(HttpServletRequest request, HttpServletResponse response, Principal principal) {
		StatusDto result;
		if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
			cronService.cleanup(minAgeToDelete, minCountToKeep);
			result = new StatusDto(0);
			LOG.info("Cleanup completed.");
		} else {
			result = new StatusDto(1);
			LOG.warning("Cleanup prevented.");
		}
		return result;
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
