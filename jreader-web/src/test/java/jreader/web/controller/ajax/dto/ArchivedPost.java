package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.ArchivedPostDto;

public class ArchivedPost extends ArchivedPostDto {
    
    private String formattedPublishDate;
    
    public String getFormattedPublishDate() {
        return formattedPublishDate;
    }
    
    @Override
    public void setPublishDate(Long publishDate) {
        super.setPublishDate(publishDate);
        this.formattedPublishDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(publishDate));
    }
    
}
