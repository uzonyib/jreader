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
public class CronJobController {

    private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());
    
    private QueueService queueService;
    
    private UserService userService;
    
    public CronJobController(final QueueService queueService, final UserService userService) {
        this.queueService = queueService;
        this.userService = userService;
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity refreshFeeds(final HttpServletRequest request) {
        final ResponseEntity result;
        if (isAuthorized(request)) {
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
        if (isAuthorized(request)) {
            queueService.cleanup();
            result = new ResponseEntity();
            LOG.info("Cleanup job added to queue.");
        } else {
            result = new ResponseEntity(HttpStatus.FORBIDDEN.value());
            LOG.warning("Cleanup job skipped.");
        }
        return result;
    }
    
    private boolean isAuthorized(final HttpServletRequest request) {
        return request.getHeader("X-AppEngine-Cron") != null || (userService.isUserLoggedIn() && userService.isUserAdmin());
    }

}
