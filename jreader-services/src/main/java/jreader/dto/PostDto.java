package jreader.dto;

import lombok.Data;

@Data
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

}
