package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.ArchivedEntryDto;

public class ArchivedEntry extends ArchivedEntryDto {
    
    private String formattedPublishedDate;
    
    public String getFormattedPublishedDate() {
        return formattedPublishedDate;
    }
    
    @Override
    public void setPublishedDate(Long publishedDate) {
        super.setPublishedDate(publishedDate);
        this.formattedPublishedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(publishedDate));
    }
    
}
