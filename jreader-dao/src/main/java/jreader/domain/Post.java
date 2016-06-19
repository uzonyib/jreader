package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache
@Data
public class Post {

    @Id
    private Long id;
    @Load
    @Parent
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<Subscription> subscriptionRef;
    @Index
    private String uri;
    private String link;
    private String title;
    private String description;
    private String author;
    @Index
    private Long publishDate;
    @Index
    private boolean read;
    @Index
    private boolean bookmarked;

    public Subscription getSubscription() {
        return getSubscriptionRef() == null ? null : getSubscriptionRef().get();
    }

    public void setSubscription(final Subscription subscription) {
        this.setSubscriptionRef(subscription == null ? null : Ref.create(subscription));
    }

}
