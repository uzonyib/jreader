package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class FeedEntry {
	
	@Id
	private Long id;
	@Load
	@Parent
	private Ref<Subscription> subscriptionRef;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Ref<Subscription> getSubscriptionRef() {
		return subscriptionRef;
	}

	public void setSubscriptionRef(Ref<Subscription> subscriptionRef) {
		this.subscriptionRef = subscriptionRef;
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
	
	public Subscription getSubscription() {
		return subscriptionRef == null ? null : subscriptionRef.get();
	}
	
	public void setSubscription(Subscription subscription) {
		this.subscriptionRef = subscription == null ? null : Ref.create(subscription);
	}

}
