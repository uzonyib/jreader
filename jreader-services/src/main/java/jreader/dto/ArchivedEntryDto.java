package jreader.dto;

public class ArchivedEntryDto {
	
	private String id;
	private String archiveId;
	private String archiveTitle;
	private String link;
	private String title;
	private String description;
	private String author;
	private Long publishedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getArchiveTitle() {
		return archiveTitle;
	}

	public void setArchiveTitle(String archiveTitle) {
		this.archiveTitle = archiveTitle;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Long publishedDate) {
		this.publishedDate = publishedDate;
	}

}
