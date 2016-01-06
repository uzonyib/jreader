package jreader.dto;

public class ArchivedEntryDto {

    private String id;
    private String archiveId;
    private String archiveTitle;
    private String link;
    private String title;
    private String description;
    private String author;
    private Long publishDate;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(final String archiveId) {
        this.archiveId = archiveId;
    }

    public String getArchiveTitle() {
        return archiveTitle;
    }

    public void setArchiveTitle(final String archiveTitle) {
        this.archiveTitle = archiveTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final Long publishDate) {
        this.publishDate = publishDate;
    }

}
