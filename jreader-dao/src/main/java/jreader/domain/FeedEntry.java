package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
public class FeedEntry {
	
	@Id
	private Long id;
	private String link;
	private String title;
	private String description;
	private String author;
	@Index
	private Long publishedDate;
	@Index
	private boolean read;
	@Index
	private boolean starred;
	@Index
	@Load
	private Ref<User> userRef;
	@Index
	@Load
	private Ref<Feed> feedRef;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Ref<User> getUserRef() {
		return userRef;
	}

	public void setUserRef(Ref<User> userRef) {
		this.userRef = userRef;
	}

	public Ref<Feed> getFeedRef() {
		return feedRef;
	}

	public void setFeedRef(Ref<Feed> feedRef) {
		this.feedRef = feedRef;
	}
	
	public User getUser() {
		return userRef == null ? null : userRef.get();
	}

	public void setUser(User user) {
		this.userRef = user == null ? null : Ref.create(user);
	}

	public Feed getFeed() {
		return feedRef == null ? null : feedRef.get();
	}
	
	public void setFeed(Feed feed) {
		this.feedRef = feed == null ? null : Ref.create(feed);
	}

}
