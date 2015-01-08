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
@RequestMapping(value = "/reader/groups")
public class GroupController {
	
	private SubscriptionService subscriptionService;
	
	public GroupController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<SubscriptionGroupDto> getSubscriptions(Principal principal) {
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public List<SubscriptionGroupDto> create(Principal principal, @RequestParam String title) {
		subscriptionService.createGroup(principal.getName(), title);
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	public List<SubscriptionGroupDto> delete(Principal principal, @PathVariable Long groupId) {
		subscriptionService.deleteGroup(principal.getName(), groupId);
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{groupId}/title", method = RequestMethod.PUT)
	public List<SubscriptionGroupDto> entitle(Principal principal, @PathVariable Long groupId, @RequestParam String value) {
		if (value != null && !"".equals(value)) {
			subscriptionService.entitle(principal.getName(), groupId, value);
		}
		
		return subscriptionService.list(principal.getName());
	}
	
	@RequestMapping(value = "/{groupId}/order", method = RequestMethod.PUT)
	public List<SubscriptionGroupDto> move(Principal principal, @PathVariable Long groupId, @RequestParam boolean up) {
		if (up) {
			subscriptionService.moveUp(principal.getName(), groupId);
		} else {
			subscriptionService.moveDown(principal.getName(), groupId);
		}
		
		return subscriptionService.list(principal.getName());
	}

}
