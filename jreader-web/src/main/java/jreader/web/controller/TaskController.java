package jreader.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import jreader.dto.FeedDto;
import jreader.dto.StatusDto;
import jreader.services.CronService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
	
	private static final Logger LOG = Logger.getLogger(TaskController.class.getName());
	
	private int minAgeToDelete;
    private int minCountToKeep;
	
	private CronService cronService;
	
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public StatusDto refreshFeeds(HttpServletRequest request, Principal principal) {
		StatusDto result;
		if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
		    List<FeedDto> feeds = cronService.listFeeds();
		    Queue queue = QueueFactory.getDefaultQueue();
		    for (FeedDto feed : feeds) {
		        queue.add(TaskOptions.Builder.withUrl("/tasks/refresh/feed").param("url", feed.getUrl()));
		    }
			result = new StatusDto(0);
		} else {
			result = new StatusDto(1);
			LOG.warning("Feed refresh prevented.");
		}
		return result;
	}
	
	@RequestMapping(value = "/refresh/feed", method = RequestMethod.POST)
    public StatusDto refreshFeed(HttpServletRequest request, Principal principal, @RequestParam String url) {
        StatusDto result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.refresh(url);
            result = new StatusDto(0);
        } else {
            result = new StatusDto(1);
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }
	
	@RequestMapping(value = "/cleanup", method = RequestMethod.POST)
    public StatusDto cleanup(HttpServletRequest request, Principal principal) {
        StatusDto result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            List<FeedDto> feeds = cronService.listFeeds();
            Queue queue = QueueFactory.getDefaultQueue();
            for (FeedDto feed : feeds) {
                queue.add(TaskOptions.Builder.withUrl("/tasks/cleanup/feed").param("url", feed.getUrl()));
            }
            result = new StatusDto(0);
        } else {
            result = new StatusDto(1);
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }
	
	@RequestMapping(value = "/cleanup/feed", method = RequestMethod.POST)
    public StatusDto cleanup(HttpServletRequest request, Principal principal, @RequestParam String url) {
        StatusDto result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.cleanup(url, minAgeToDelete, minCountToKeep);
            result = new StatusDto(0);
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
