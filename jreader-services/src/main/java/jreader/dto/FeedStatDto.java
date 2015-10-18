package jreader.dto;

public class FeedStatDto {

    private final long date;
    private int count;
    
    public FeedStatDto(final long date) {
        this.date = date;
        this.count = 0;
    }
    
    public long getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count += count;
    }

}
