package jreader.web.controller.appengine;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jreader.web.service.QueueService;

@RestController
@RequestMapping("/cron")
public class CronJobController {

    private static final Logger LOG = Logger.getLogger(CronJobController.class.getName());

    private QueueService queueService;

    @Autowired
    public CronJobController(final QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/refresh")
    public void refresh() {
        queueService.refresh();
        LOG.info("Refresh job added to queue.");
    }

    @GetMapping("/cleanup")
    public void cleanup() {
        queueService.cleanup();
        LOG.info("Cleanup job added to queue.");
    }

}
