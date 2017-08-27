package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.StatService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping("/reader/stats")
public class StatController {

    private final StatService statService;

    private final int daysToDisplayStats;

    @Autowired
    public StatController(final StatService statService, @Value("${daysToDisplayStats}") final int daysToDisplayStats) {
        this.statService = statService;
        this.daysToDisplayStats = daysToDisplayStats;
    }

    @GetMapping
    public ResponseEntity list(final Principal principal) {
        return new ResponseEntity(statService.list(principal.getName(), daysToDisplayStats));
    }

}
