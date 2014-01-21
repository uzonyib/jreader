package jreader.services;

import jreader.common.FeedEntryFilter;

public class FeedEntryFilterData extends FeedEntryFilter {

	public static enum Group {
		ALL, SUBSCRIPTION_GROUP, SUBSCRIPTION;
	}
	
	private String username;
	private Group group;
	private Long subscriptionGroupId;
	private Long subscriptionId;
	
	public FeedEntryFilterData(String username, Selection selection, boolean ascending, int offset, int count) {
		super(selection, ascending, offset, count);
		this.username = username;
		this.group = Group.ALL;
	}

	public FeedEntryFilterData(String username, Long subscriptionGroupId, Selection selection, boolean ascending, int offset, int count) {
		super(selection, ascending, offset, count);
		this.username = username;
		this.subscriptionGroupId = subscriptionGroupId;
		this.group = Group.SUBSCRIPTION_GROUP;
	}

	public FeedEntryFilterData(String username, Long subscriptionGroupId, Long subscriptionId, Selection selection, boolean ascending, int offset, int count) {
		super(selection, ascending, offset, count);
		this.username = username;
		this.subscriptionGroupId = subscriptionGroupId;
		this.subscriptionId = subscriptionId;
		this.group = Group.SUBSCRIPTION;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Long getSubscriptionGroupId() {
		return subscriptionGroupId;
	}

	public void setSubscriptionGroupId(Long subscriptionGroupId) {
		this.subscriptionGroupId = subscriptionGroupId;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

}
