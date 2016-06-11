package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.PostDto;

public class Post {
    
    private final PostDto dto;
    
    public Post(PostDto dto) {
        this.dto = dto;
    }
    
    public String getSubscriptionTitle() {
        return dto.getSubscriptionTitle();
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
    
    public boolean isRead() {
        return dto.isRead();
    }
    
    public boolean isBookmarked() {
        return dto.isBookmarked();
    }

    public String getFormattedPublishDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(dto.getPublishDate()));
    }
    
}
