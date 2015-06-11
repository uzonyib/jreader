package jreader.dto;

public class FeedEntryDto {

    private String id;
    private String subscriptionId;
    private String subscriptionGroupId;
    private String subscriptionTitle;
    private String link;
    private String title;
    private String description;
    private String author;
    private Long publishedDate;
    private boolean read;
    private boolean starred;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionGroupId() {
        return subscriptionGroupId;
    }

    public void setSubscriptionGroupId(final String subscriptionGroupId) {
        this.subscriptionGroupId = subscriptionGroupId;
    }

    public String getSubscriptionTitle() {
        return subscriptionTitle;
    }

    public void setSubscriptionTitle(final String subscriptionTitle) {
        this.subscriptionTitle = subscriptionTitle;
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

}
