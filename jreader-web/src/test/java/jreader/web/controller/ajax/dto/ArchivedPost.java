package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.ArchivedPostDto;

public class ArchivedPost {
    
    private final ArchivedPostDto dto;
    
    public ArchivedPost(ArchivedPostDto dto) {
        this.dto = dto;
    }
    
    public String getArchiveTitle() {
        return dto.getArchiveTitle();
    }
    
    public String getTitle() {
        return dto.getTitle();
    }
    
    public String getDescription() {
        return dto.getDescription();
    }
    
    public String getAuthor() {
        return dto.getAuthor();
    }
    
    public String getLink() {
        return dto.getLink();
    }

    public String getFormattedPublishDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(dto.getPublishDate()));
    }
    
}
