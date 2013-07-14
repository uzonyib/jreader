package jreader.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Subscription {
	
	@Id
	private Long id;
	@Parent
	private Key<User> user;
	@Index
	@Load
	private Ref<Feed> feed;
	private String title;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<User> getUser() {
		return user;
	}

	public void setUser(Key<User> user) {
		this.user = user;
	}

	public Ref<Feed> getFeed() {
		return feed;
	}

	public void setFeed(Ref<Feed> feed) {
		this.feed = feed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
