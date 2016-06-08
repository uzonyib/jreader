package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.FeedStatDto;

public class FeedStat {
    
    private FeedStatDto dto;

    public FeedStat(FeedStatDto dto) {
        this.dto = dto;
    }
    
    public int getCount() {
        return dto.getCount();
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(dto.getDate()));
    }

}
