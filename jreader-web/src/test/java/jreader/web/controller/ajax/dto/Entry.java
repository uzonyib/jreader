package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.FeedEntryDto;

public class Entry extends FeedEntryDto {
    
    private String formattedPublishedDate;
    
    public String getFormattedPublishedDate() {
        return formattedPublishedDate;
    }
    
    @Override
    public void setPublishedDate(Long publishedDate) {
        super.setPublishedDate(publishedDate);
        this.formattedPublishedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(publishedDate));
    }
    
}
