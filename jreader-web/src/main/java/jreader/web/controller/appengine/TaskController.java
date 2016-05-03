package jreader.web.controller.appengine;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.users.UserService;

import jreader.dto.FeedDto;
import jreader.services.CronService;
import jreader.web.controller.ResponseEntity;
import jreader.web.service.QueueService;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController extends AppengineController {

    private static final Logger LOG = Logger.getLogger(TaskController.class.getName());

    private CronService cronService;
    
    private QueueService queueService;
    
    private final int daysToKeepPosts;
    private final int postsToKeep;
    private final int statsToKeep;

    public TaskController(final CronService cronService, final QueueService queueService, final UserService userService, final int daysToKeepPosts,
            final int postsToKeep, final int statsToKeep) {
        super(userService);
        this.cronService = cronService;
        this.queueService = queueService;
        this.daysToKeepPosts = daysToKeepPosts;
        this.postsToKeep = postsToKeep;
        this.statsToKeep = statsToKeep;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity refreshFeeds(final HttpServletRequest request) {
        final ResponseEntity result;
        if (isAppengineTaskRequest(request) || isUserAuthorized()) {
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
    public ResponseEntity refreshFeed(final HttpServletRequest request, @RequestParam final String url) {
        final ResponseEntity result;
        if (isAppengineTaskRequest(request) || isUserAuthorized()) {
            cronService.refresh(url);
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Feed refresh prevented.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup", method = RequestMethod.POST)
    public ResponseEntity cleanup(final HttpServletRequest request) {
        final ResponseEntity result;
        if (isAppengineTaskRequest(request) || isUserAuthorized()) {
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
    public ResponseEntity cleanup(final HttpServletRequest request, @RequestParam final String url) {
        final ResponseEntity result;
        if (isAppengineTaskRequest(request) || isUserAuthorized()) {
            cronService.cleanup(url, daysToKeepPosts, postsToKeep, statsToKeep);
            result = new ResponseEntity();
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup prevented.");
        }
        return result;
    }

}
