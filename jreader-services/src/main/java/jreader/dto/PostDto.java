package jreader.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostDto {

    private String id;
    private String subscriptionId;
    private String subscriptionTitle;
    private String groupId;
    private String link;
    private String title;
    private String description;
    private String author;
    private Long publishDate;
    private boolean read;
    private boolean bookmarked;

}
