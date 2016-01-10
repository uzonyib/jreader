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
public class Post {

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
    private Long publishDate;
    @Index
    private boolean read;
    @Index
    private boolean bookmarked;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    Ref<Subscription> getSubscriptionRef() {
        return subscriptionRef;
    }

    void setSubscriptionRef(final Ref<Subscription> subscriptionRef) {
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

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final Long publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public boolean isBookMarked() {
        return bookmarked;
    }

    public void setBookmarked(final boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public Subscription getSubscription() {
        return getSubscriptionRef() == null ? null : getSubscriptionRef().get();
    }

    public void setSubscription(final Subscription subscription) {
        this.setSubscriptionRef(subscription == null ? null : Ref.create(subscription));
    }

}
