package jreader.services;

import java.util.List;

import jreader.dto.FeedDto;

public interface CronService {

    List<FeedDto> listFeeds();

    void refresh(String url);

    void cleanup(String url, int olderThanDays, int keptCount);

}
