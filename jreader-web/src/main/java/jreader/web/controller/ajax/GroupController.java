package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import jreader.dto.SubscriptionGroupDto;
import jreader.services.SubscriptionService;
import jreader.web.controller.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader/groups")
public class GroupController {

    private final SubscriptionService subscriptionService;

    public GroupController(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SubscriptionGroupDto>> listAll(final Principal principal) {
        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<SubscriptionGroupDto>> create(final Principal principal, @RequestParam final String title) {
        subscriptionService.createGroup(principal.getName(), title);
        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
    public ResponseEntity<List<SubscriptionGroupDto>> delete(final Principal principal, @PathVariable final Long groupId) {
        subscriptionService.deleteGroup(principal.getName(), groupId);
        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}/title", method = RequestMethod.PUT)
    public ResponseEntity<List<SubscriptionGroupDto>> entitle(final Principal principal, @PathVariable final Long groupId, @RequestParam final String value) {
        subscriptionService.entitle(principal.getName(), groupId, value);
        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}/order", method = RequestMethod.PUT)
    public ResponseEntity<List<SubscriptionGroupDto>> move(final Principal principal, @PathVariable final Long groupId, @RequestParam final boolean up) {
        if (up) {
            subscriptionService.moveUp(principal.getName(), groupId);
        } else {
            subscriptionService.moveDown(principal.getName(), groupId);
        }

        return new ResponseEntity<List<SubscriptionGroupDto>>(subscriptionService.list(principal.getName()));
    }

}
