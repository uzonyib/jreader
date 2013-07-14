package jreader.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class FeedEntry {
	
	@Id
	private String id;
	private String link;
	private String title;
	private String description;
	private String author;
	@Index
	private Long publishedDate;
	@Parent
	private Key<Feed> feed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getPublishDate() {
		return publishedDate;
	}

	public void setPublishDate(Long publishDate) {
		this.publishedDate = publishDate;
	}

	public Long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Key<Feed> getFeed() {
		return feed;
	}

	public void setFeed(Key<Feed> feed) {
		this.feed = feed;
	}

}
