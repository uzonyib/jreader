package jreader.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Feed {

    @Id
    private String url;
    private String title;
    private String description;
    private String feedType;
    private Long refreshDate;
    private Long publishedDate;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
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

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(final String feedType) {
        this.feedType = feedType;
    }

    public Long getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Long refreshDate) {
        this.refreshDate = refreshDate;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(final Long publishedDate) {
        this.publishedDate = publishedDate;
    }

}
