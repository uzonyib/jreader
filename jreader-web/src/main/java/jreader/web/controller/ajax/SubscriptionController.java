package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import jreader.dto.SubscriptionGroupDto;
import jreader.services.SubscriptionService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/reader/groups/{groupId}/subscriptions")
public class SubscriptionController {
	
	private SubscriptionService subscriptionService;
	
	public SubscriptionController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public List<SubscriptionGroupDto> create(Principal principal, @PathVariable Long groupId, @RequestParam String url) {
		subscriptionService.subscribe(principal.getName(), groupId, url);
		return subscriptionService.list(principal.getName());
	}

	@RequestMapping(value = "/{subscriptionId}", method = RequestMethod.DELETE)
	public List<SubscriptionGroupDto> delete(Principal principal, @PathVariable Long groupId, @PathVariable Long subscriptionId) {
		subscriptionService.unsubscribe(principal.getName(), groupId, subscriptionId);
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{subscriptionId}/title", method = RequestMethod.PUT)
	public List<SubscriptionGroupDto> entitle(Principal principal, @PathVariable Long groupId, @PathVariable Long subscriptionId, @RequestParam String value) {
		if (value != null && !"".equals(value)) {
			subscriptionService.entitle(principal.getName(), groupId, subscriptionId, value);
		}
		
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{subscriptionId}/order", method = RequestMethod.PUT)
	public List<SubscriptionGroupDto> move(Principal principal, @PathVariable Long groupId, @PathVariable Long subscriptionId, @RequestParam boolean up) {
		if (up) {
			subscriptionService.moveUp(principal.getName(), groupId, subscriptionId);
		} else {
			subscriptionService.moveDown(principal.getName(), groupId, subscriptionId);
		}
		
		return subscriptionService.list(principal.getName());
	}

}
