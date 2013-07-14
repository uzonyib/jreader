package jreader.rss.domain;

import java.util.List;

public class Feed {
	
	private String url;
	private String title;
	private String description;
	private String feedType;
	private List<String> categories;
	private Long publishedDate;
	private List<FeedEntry> entries;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
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

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}

	public List<FeedEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<FeedEntry> entries) {
		this.entries = entries;
	}

}
