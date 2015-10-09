package jreader.web.controller.appengine;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jreader.web.controller.ResponseEntity;
import jreader.web.service.QueueService;

@RestController
@RequestMapping(value = "/cron")
public class CronJobController {

    private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
    
    private QueueService queueService;
    
    public CronJobController(final QueueService queueService) {
        this.queueService = queueService;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity refreshFeeds(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
            queueService.refresh();
            result = new ResponseEntity();
            LOG.info("Refresh job added to queue.");
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Refresh job skipped.");
        }
        return result;
    }

    @RequestMapping(value = "/cleanup", method = RequestMethod.GET)
    public ResponseEntity cleanup(final HttpServletRequest request, final Principal principal) {
        final ResponseEntity result;
        if (request.getHeader("X-AppEngine-Cron") != null || principal != null) {
            queueService.cleanup();
            result = new ResponseEntity();
            LOG.info("Cleanup job added to queue.");
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup job skipped.");
        }
        return result;
    }

}
