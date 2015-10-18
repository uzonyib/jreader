package jreader.dto;

import java.util.List;

public class FeedStatsDto {

    private final FeedDto feed;
    private final List<FeedStatDto> stats;
    
    public FeedStatsDto(final FeedDto feed, final List<FeedStatDto> stats) {
        this.feed = feed;
        this.stats = stats;
    }

    public FeedDto getFeed() {
        return feed;
    }

    public List<FeedStatDto> getStats() {
        return stats;
    }

}
