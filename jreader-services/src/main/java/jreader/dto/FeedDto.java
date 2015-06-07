package jreader.dto;

public class FeedDto {

    private String title;
    private String url;
    private String description;
    private String feedType;
    private Long publishedDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Long publishedDate) {
        this.publishedDate = publishedDate;
    }

}
