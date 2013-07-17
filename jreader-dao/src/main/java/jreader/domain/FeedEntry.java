package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

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
	@Index
	@Load
	private Ref<Feed> feedRef;

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

	public Long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}

	public Ref<Feed> getFeedRef() {
		return feedRef;
	}

	public void setFeedRef(Ref<Feed> feedRef) {
		this.feedRef = feedRef;
	}

	public Feed getFeed() {
		return feedRef == null ? null : feedRef.get();
	}
	
	public void setFeed(Feed feed) {
		this.feedRef = feed == null ? null : Ref.create(feed);
	}

}
