package jreader.services;

import java.util.List;

import jreader.dto.FeedStatsDto;

public interface StatService {

    List<FeedStatsDto> list(String username, int days);

}
