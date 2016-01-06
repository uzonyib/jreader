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
    private Long updatedDate;
    private Long refreshDate;
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

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(final Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getRefreshDate() {
        return refreshDate;
    }
    
    public void setRefreshDate(final Long refreshDate) {
        this.refreshDate = refreshDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

}
