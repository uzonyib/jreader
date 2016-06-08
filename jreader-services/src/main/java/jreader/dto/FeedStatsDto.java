package jreader.dto;

import java.util.List;

import lombok.Value;

@Value
public class FeedStatsDto {

    private final FeedDto feed;
    private final List<FeedStatDto> stats;

}
