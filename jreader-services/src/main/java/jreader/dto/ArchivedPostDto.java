package jreader.dto;

import lombok.Data;

@Data
public class ArchivedPostDto {

    private String id;
    private String archiveId;
    private String archiveTitle;
    private String link;
    private String title;
    private String description;
    private String author;
    private Long publishDate;

}
