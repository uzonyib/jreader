package jreader.web.controller.appengine;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/cron")
public class CronJobController {

    private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<Void> refreshFeeds(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
            final Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.withUrl("/tasks/refresh"));
            result = new ResponseEntity<Void>();
            LOG.info("Refresh job added to queue.");
        } else {
            result = new ResponseEntity<Void>(HttpStatus.FORBIDDEN.value());
            LOG.warning("Refresh job skipped.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup", method = RequestMethod.GET)
    public ResponseEntity<Void> cleanup(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity<Void> result;
        if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
            final Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.withUrl("/tasks/cleanup"));
            result = new ResponseEntity<Void>();
            LOG.info("Cleanup job added to queue.");
        } else {
            result = new ResponseEntity<Void>(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup job skipped.");
        }
        return result;
    }

}
