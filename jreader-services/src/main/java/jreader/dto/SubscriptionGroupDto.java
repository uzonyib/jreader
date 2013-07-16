package jreader.dto;

import java.util.List;

public class SubscriptionGroupDto {
	
	private String title;
	private List<SubscriptionDto> subscriptions;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SubscriptionDto> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<SubscriptionDto> subscriptions) {
		this.subscriptions = subscriptions;
	}

}
