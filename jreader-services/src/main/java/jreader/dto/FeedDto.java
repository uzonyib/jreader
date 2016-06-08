package jreader.dto;

import lombok.Data;

@Data
public class FeedDto {

    private String title;
    private String url;
    private String description;
    private String feedType;
    private Long lastUpdateDate;
    private Long lastRefreshDate;
    private Integer status;

}
