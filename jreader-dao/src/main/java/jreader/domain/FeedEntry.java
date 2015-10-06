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
public class FeedEntry {

    @Id
    private Long id;
    @Load
    @Parent
    private Ref<Subscription> subscriptionRef;
    @Index
    private String uri;
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

    public void setId(final Long id) {
        this.id = id;
    }

    public Ref<Subscription> getSubscriptionRef() {
        return subscriptionRef;
    }

    public void setSubscriptionRef(final Ref<Subscription> subscriptionRef) {
        this.subscriptionRef = subscriptionRef;
    }
    
    public String getUri() {
        return uri;
    }
    
    public void setUri(final String uri) {
        this.uri = uri;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(final Long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(final boolean starred) {
        this.starred = starred;
    }

    public Subscription getSubscription() {
        return getSubscriptionRef() == null ? null : getSubscriptionRef().get();
    }

    public void setSubscription(final Subscription subscription) {
        this.setSubscriptionRef(subscription == null ? null : Ref.create(subscription));
    }

}
