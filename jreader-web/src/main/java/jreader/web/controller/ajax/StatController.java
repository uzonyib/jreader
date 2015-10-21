package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.StatService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/reader/stats")
public class StatController {

    private final StatService statService;
    
    private final int daysToDisplayStats;

    public StatController(final StatService statService, final int daysToDisplayStats) {
        this.statService = statService;
        this.daysToDisplayStats = daysToDisplayStats;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity list(final Principal principal) {
        return new ResponseEntity(statService.list(principal.getName(), daysToDisplayStats));
    }

}