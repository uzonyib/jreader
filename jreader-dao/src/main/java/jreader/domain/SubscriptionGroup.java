package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class SubscriptionGroup {

	@Id
	private Long id;
	@Load
	@Parent
	private Ref<User> userRef;
	@Index
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
