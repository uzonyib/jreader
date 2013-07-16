package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Action {
	
	@Id
	private Long id;
	@Parent
	@Load
	private Ref<User> userRef;
	@Index
	@Load
	private Ref<FeedEntry> feedEntryRef;
	@Index
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ref<User> getUserRef() {
		return userRef;
	}

	public void setUserRef(Ref<User> userRef) {
		this.userRef = userRef;
	}

	public Ref<FeedEntry> getFeedEntryRef() {
		return feedEntryRef;
	}

	public void setFeedEntryRef(Ref<FeedEntry> feedEntryRef) {
		this.feedEntryRef = feedEntryRef;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public User getUser() {
		return userRef == null ? null : userRef.get();
	}

	public void setUser(User user) {
		this.userRef = user == null ? null : Ref.create(user);
	}
	
	public FeedEntry getFeedEntry() {
		return feedEntryRef == null ? null : feedEntryRef.get();
	}

	public void setFeedEntry(FeedEntry feedEntry) {
		this.feedEntryRef = feedEntry == null ? null : Ref.create(feedEntry);
	}

}
