package jreader.web.controller.appengine;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.FeedDto;
import jreader.services.CronService;
import jreader.web.controller.ResponseEntity;
import jreader.web.service.QueueService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private static final Logger LOG = Logger.getLogger(TaskController.class.getName());

    private final CronService cronService;
    
    private QueueService queueService;

    private final int daysToKeepEntries;
    private final int entriesToKeep;
    private final int statsToKeep;

    public TaskController(final CronService cronService, final QueueService queueService, final int daysToKeepEntries, final int entriesToKeep,
            final int statsToKeep) {
        this.cronService = cronService;
        this.queueService = queueService;
        this.daysToKeepEntries = daysToKeepEntries;
        this.entriesToKeep = entriesToKeep;
        this.statsToKeep = statsToKeep;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity refreshFeeds(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            final List<FeedDto> feeds = cronService.listFeeds();
            for (final FeedDto feed : feeds) {
                queueService.refresh(feed.getUrl());
            }
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/refresh/feed", method = RequestMethod.POST)
    public ResponseEntity refreshFeed(final HttpServletRequest request, final Principal principal, @RequestParam final String url) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.refresh(url);
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup", method = RequestMethod.POST)
    public ResponseEntity cleanup(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            final List<FeedDto> feeds = cronService.listFeeds();
            for (FeedDto feed : feeds) {
                queueService.cleanup(feed.getUrl());
            }
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup/feed", method = RequestMethod.POST)
    public ResponseEntity cleanup(final HttpServletRequest request, final Principal principal, @RequestParam final String url) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.cleanup(url, daysToKeepEntries, entriesToKeep, statsToKeep);
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }

}
