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
    private Long lastUpdateDate;
    private Long lastRefreshDate;
    private Integer status;

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

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(final Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastRefreshDate() {
        return lastRefreshDate;
    }
    
    public void setLastRefreshDate(final Long lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

}
