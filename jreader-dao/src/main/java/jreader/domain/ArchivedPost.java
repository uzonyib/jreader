package jreader.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache
@Data
public class ArchivedPost {

    @Id
    private Long id;
    @Load
    @Parent
    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.PACKAGE)
    private Ref<Archive> archiveRef;
    private String link;
    private String title;
    private String description;
    private String author;
    @Index
    private Long publishDate;

    public Archive getArchive() {
        return getArchiveRef() == null ? null : getArchiveRef().get();
    }

    public void setArchive(final Archive archive) {
        this.setArchiveRef(archive == null ? null : Ref.create(archive));
    }

}
