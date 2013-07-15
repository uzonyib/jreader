package jreader.dto;

public class SubscriptionDto {
	
	private String title;
	private FeedDto feed;
	private SubscriptionGroupDto group;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public FeedDto getFeed() {
		return feed;
	}

	public void setFeed(FeedDto feed) {
		this.feed = feed;
	}

	public SubscriptionGroupDto getGroup() {
		return group;
	}

	public void setGroup(SubscriptionGroupDto group) {
		this.group = group;
	}

}
