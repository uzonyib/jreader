package jreader.web.controller.appengine;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

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

    private final int minAgeToDelete;
    private final int minCountToKeep;

    public TaskController(final CronService cronService, final QueueService queueService, final int minAgeToDelete, final int minCountToKeep) {
        this.cronService = cronService;
        this.queueService = queueService;
        this.minAgeToDelete = minAgeToDelete;
        this.minCountToKeep = minCountToKeep;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshFeeds(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            final List<FeedDto> feeds = cronService.listFeeds();
            for (final FeedDto feed : feeds) {
                queueService.refresh(feed.getUrl());
            }
            result = new ResponseEntity<Void>(0);
        } else {
            result = new ResponseEntity<Void>(1);
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/refresh/feed", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshFeed(final HttpServletRequest request, final Principal principal, @RequestParam final String url) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.refresh(url);
            result = new ResponseEntity<Void>(0);
        } else {
            result = new ResponseEntity<Void>(1);
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup", method = RequestMethod.POST)
    public ResponseEntity<Void> cleanup(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            final List<FeedDto> feeds = cronService.listFeeds();
            for (FeedDto feed : feeds) {
                queueService.cleanup(feed.getUrl());
            }
            result = new ResponseEntity<Void>(0);
        } else {
            result = new ResponseEntity<Void>(1);
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup/feed", method = RequestMethod.POST)
    public ResponseEntity<Void> cleanup(final HttpServletRequest request, final Principal principal, @RequestParam final String url) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-TaskName") != null || principal != null) {
            cronService.cleanup(url, minAgeToDelete, minCountToKeep);
            result = new ResponseEntity<Void>(0);
        } else {
            result = new ResponseEntity<Void>(1);
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }

}
