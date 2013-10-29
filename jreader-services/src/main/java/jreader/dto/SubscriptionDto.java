package jreader.dto;

public class SubscriptionDto {
	
	private String id;
	private String title;
	private FeedDto feed;
	private int unreadCount;
	private Long updatedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public int getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

}
