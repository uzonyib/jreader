package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.GroupDto;
import jreader.dto.SubscriptionDto;
import jreader.services.GroupService;
import jreader.services.SubscriptionService;
import jreader.web.service.QueueService;

@RestController
@RequestMapping("/reader/groups/{groupId}/subscriptions")
public class SubscriptionController {

    private final GroupService groupService;

    private final SubscriptionService subscriptionService;

    private final QueueService queueService;

    @Autowired
    public SubscriptionController(final GroupService groupService, final SubscriptionService subscriptionService,
            final QueueService queueService) {
        this.groupService = groupService;
        this.subscriptionService = subscriptionService;
        this.queueService = queueService;
    }

    @PostMapping
    public List<GroupDto> create(final Principal principal, @PathVariable final Long groupId, @RequestParam final String url) {
        final SubscriptionDto subscription = subscriptionService.subscribe(principal.getName(), groupId, url);
        queueService.refresh(subscription.getFeed().getUrl());
        return groupService.list(principal.getName());
    }

    @DeleteMapping("/{subscriptionId}")
    public List<GroupDto> delete(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId) {
        subscriptionService.unsubscribe(principal.getName(), groupId, subscriptionId);
        return groupService.list(principal.getName());
    }

    @PutMapping("/{subscriptionId}/title")
    public List<GroupDto> entitle(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId, @RequestParam final String value) {
        subscriptionService.entitle(principal.getName(), groupId, subscriptionId, value);
        return groupService.list(principal.getName());
    }

    @PutMapping("/{subscriptionId}/order")
    public List<GroupDto> move(final Principal principal, @PathVariable final Long groupId,
            @PathVariable final Long subscriptionId, @RequestParam final boolean up) {
        if (up) {
            subscriptionService.moveUp(principal.getName(), groupId, subscriptionId);
        } else {
            subscriptionService.moveDown(principal.getName(), groupId, subscriptionId);
        }

        return groupService.list(principal.getName());
    }

}
