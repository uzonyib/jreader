package jreader.web.controller.appengine;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.users.UserService;

import jreader.web.controller.ResponseEntity;
import jreader.web.service.QueueService;

@RestController
@RequestMapping(value = "/cron")
public class CronJobController extends AppengineController {

    private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
    
    private QueueService queueService;
    
    public CronJobController(final QueueService queueService, final UserService userService) {
        super(userService);
        this.queueService = queueService;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity refreshFeeds(final HttpServletRequest request) {
        final ResponseEntity result;
        if (isAppengineCronRequest(request) || isUserAuthorized()) {
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
    public ResponseEntity cleanup(final HttpServletRequest request) {
        final ResponseEntity result;
        if (isAppengineCronRequest(request) || isUserAuthorized()) {
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
