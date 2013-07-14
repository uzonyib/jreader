package jreader.dto;


public class FeedEntryDto {
	
	private Long id;
	private String link;
	private String title;
	private String description;
	private String author;
	private Long publishedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
