package jreader.dao;

public class ArchivedEntryFilter {

    protected boolean ascending;
    protected int offset;
    protected int count;

    public ArchivedEntryFilter(boolean ascending, int offset, int count) {
        this.ascending = ascending;
        this.offset = offset;
        this.count = count;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
