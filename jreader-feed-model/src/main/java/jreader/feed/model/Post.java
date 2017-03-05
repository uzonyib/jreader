package jreader.feed.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {

    private String title;
    private String description;
    private String author;
    private String url;
    private Long publishDate;

}
