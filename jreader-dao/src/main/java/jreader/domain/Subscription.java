package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Subscription {
	
	@Id
	private Long id;
	@Index
	@Load
	private Ref<User> userRef;
	@Index
	@Load
	private Ref<Feed> feedRef;
	@Index
	@Load
	private Ref<SubscriptionGroup> groupRef;
	private String title;
	@Index
	private int order;

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

	public Ref<Feed> getFeedRef() {
		return feedRef;
	}

	public void setFeedRef(Ref<Feed> feedRef) {
		this.feedRef = feedRef;
	}

	public Ref<SubscriptionGroup> getGroupRef() {
		return groupRef;
	}

	public void setGroupRef(Ref<SubscriptionGroup> groupRef) {
		this.groupRef = groupRef;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public SubscriptionGroup getGroup() {
		return groupRef == null ? null : groupRef.get();
	}

	public void setGroup(SubscriptionGroup group) {
		this.groupRef = group == null ? null : Ref.create(group);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
