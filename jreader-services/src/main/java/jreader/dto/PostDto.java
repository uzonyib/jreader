package jreader.dto;

public class PostDto {

    private String id;
    private String subscriptionId;
    private String groupId;
    private String subscriptionTitle;
    private String link;
    private String title;
    private String description;
    private String author;
    private Long publishDate;
    private boolean read;
    private boolean bookmarked;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
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

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(final boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

}