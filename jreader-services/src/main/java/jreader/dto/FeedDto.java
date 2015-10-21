package jreader.dto;

public class FeedDto {

    private String title;
    private String url;
    private String description;
    private String feedType;
    private Long publishedDate;
    private Long refreshDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(final String feedType) {
        this.feedType = feedType;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(final Long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(final Long refreshDate) {
        this.refreshDate = refreshDate;
    }

}
