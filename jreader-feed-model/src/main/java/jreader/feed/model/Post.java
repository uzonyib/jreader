package jreader.feed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private String title;
    private String description;
    private String author;
    private String url;
    private Long publishDate;

}
