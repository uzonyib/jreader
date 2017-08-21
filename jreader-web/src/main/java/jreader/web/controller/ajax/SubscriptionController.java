package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.SubscriptionDto;
import jreader.services.GroupService;
import jreader.services.SubscriptionService;
import jreader.web.controller.ResponseEntity;
import jreader.web.service.QueueService;

@RestController
@RequestMapping(value = "/reader/groups/{groupId}/subscriptions")
public class SubscriptionController {

    private final GroupService groupService;

    private final SubscriptionService subscriptionService;

    private QueueService queueService;

    @Autowired
    public SubscriptionController(final GroupService groupService, final SubscriptionService subscriptionService,
            final QueueService queueService) {
        this.groupService = groupService;
        this.subscriptionService = subscriptionService;
        this.queueService = queueService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(final Principal principal, @PathVariable final Long groupId, @RequestParam final String url) {
        final SubscriptionDto subscription = subscriptionService.subscribe(principal.getName(), groupId, url);
        queueService.refresh(subscription.getFeed().getUrl());
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{subscriptionId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId) {
        subscriptionService.unsubscribe(principal.getName(), groupId, subscriptionId);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{subscriptionId}/title", method = RequestMethod.PUT)
    public ResponseEntity entitle(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId, @RequestParam final String value) {
        subscriptionService.entitle(principal.getName(), groupId, subscriptionId, value);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{subscriptionId}/order", method = RequestMethod.PUT)
    public ResponseEntity move(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId, @RequestParam final boolean up) {
        if (up) {
            subscriptionService.moveUp(principal.getName(), groupId, subscriptionId);
        } else {
            subscriptionService.moveDown(principal.getName(), groupId, subscriptionId);
        }

        return new ResponseEntity(groupService.list(principal.getName()));
    }

}
