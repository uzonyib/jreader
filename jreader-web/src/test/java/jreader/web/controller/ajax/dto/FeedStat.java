package jreader.web.controller.ajax.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jreader.dto.FeedStatDto;

public class FeedStat extends FeedStatDto {

    public FeedStat(long date) {
        super(date);
    }
    
    public FeedStat(FeedStatDto dto) {
        this(dto.getDate());
        this.addCount(dto.getCount());
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").format(new Date(getDate()));
    }

}
