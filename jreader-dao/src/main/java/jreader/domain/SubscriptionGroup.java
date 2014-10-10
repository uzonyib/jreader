package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
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
    
    public SubscriptionGroup() {
        
    }

    private SubscriptionGroup(final Builder builder) {
        this.setId(builder.id);
        this.setUser(builder.user);
        this.setTitle(builder.title);
        this.setOrder(builder.order);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Ref<User> getUserRef() {
        return userRef;
    }

    public void setUserRef(final Ref<User> userRef) {
        this.userRef = userRef;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public User getUser() {
        return getUserRef() == null ? null : getUserRef().get();
    }

    public void setUser(final User user) {
        this.setUserRef(user == null ? null : Ref.create(user));
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }
    
    public static class Builder {

        private Long id;
        private User user;
        private String title;
        private int order;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder user(final User user) {
            this.user = user;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder order(final int order) {
            this.order = order;
            return this;
        }
        
        public SubscriptionGroup build() {
            return new SubscriptionGroup(this);
        }

    }

}
