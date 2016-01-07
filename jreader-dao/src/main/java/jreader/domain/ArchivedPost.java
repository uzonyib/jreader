package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class ArchivedPost {

    @Id
    private Long id;
    @Load
    @Parent
    private Ref<Archive> archiveRef;
    private String link;
    private String title;
    private String description;
    private String author;
    @Index
    private Long publishDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Ref<Archive> getArchiveRef() {
        return archiveRef;
    }

    public void setArchiveRef(final Ref<Archive> archiveRef) {
        this.archiveRef = archiveRef;
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

    public Archive getArchive() {
        return getArchiveRef() == null ? null : getArchiveRef().get();
    }

    public void setArchive(final Archive archive) {
        this.setArchiveRef(archive == null ? null : Ref.create(archive));
    }

}
