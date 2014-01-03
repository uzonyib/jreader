package jreader.dto;

public class FeedEntryDto {
	
	private String id;
	private String feedId;
	private String subscriptionId;
	private String subscriptionGroupId;
	private String subscriptionTitle;
	private String link;
	private String title;
	private String description;
	private String author;
	private Long publishedDate;
	private boolean read;
	private boolean starred;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeedId() {
		return feedId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionGroupId() {
		return subscriptionGroupId;
	}

	public void setSubscriptionGroupId(String subscriptionGroupId) {
		this.subscriptionGroupId = subscriptionGroupId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getSubscriptionTitle() {
		return subscriptionTitle;
	}

	public void setSubscriptionTitle(String subscriptionTitle) {
		this.subscriptionTitle = subscriptionTitle;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

}
