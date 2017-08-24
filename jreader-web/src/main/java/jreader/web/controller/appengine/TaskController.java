package jreader.web.controller.appengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.CronService;
import jreader.web.service.QueueService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private CronService cronService;
    private QueueService queueService;

    private final int daysToKeepPosts;
    private final int postsToKeep;
    private final int statsToKeep;

    @Autowired
    public TaskController(final CronService cronService, final QueueService queueService, @Value("${daysToKeepPosts}") final int daysToKeepPosts,
            @Value("${postsToKeep}") final int postsToKeep, @Value("${daysToKeepStats}") final int statsToKeep) {
        this.cronService = cronService;
        this.queueService = queueService;
        this.daysToKeepPosts = daysToKeepPosts;
        this.postsToKeep = postsToKeep;
        this.statsToKeep = statsToKeep;
    }

    @PostMapping("/refresh")
    public void refreshFeeds() {
        cronService.listFeeds().forEach(feed -> queueService.refresh(feed.getUrl()));
    }

    @PostMapping("/refresh/feed")
    public void refreshFeed(@RequestParam final String url) {
        cronService.refresh(url);
    }

    @PostMapping("/cleanup")
    public void cleanup() {
        cronService.listFeeds().forEach(feed -> queueService.cleanup(feed.getUrl()));
    }

    @PostMapping("/cleanup/feed")
    public void cleanup(@RequestParam final String url) {
        cronService.cleanup(url, daysToKeepPosts, postsToKeep, statsToKeep);
    }

}
