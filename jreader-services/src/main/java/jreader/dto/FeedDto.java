package jreader.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FeedDto {

    private String url;
    private String title;
    private String description;
    private String feedType;
    private Long lastUpdateDate;
    private Long lastRefreshDate;
    private Integer status;

}
