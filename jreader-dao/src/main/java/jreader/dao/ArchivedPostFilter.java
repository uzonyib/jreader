package jreader.dao;

public class ArchivedPostFilter {

    private boolean ascending;
    private int offset;
    private int count;

    public ArchivedPostFilter(final boolean ascending, final int offset, final int count) {
        this.ascending = ascending;
        this.offset = offset;
        this.count = count;
    }

    public final boolean isAscending() {
        return ascending;
    }

    public final int getOffset() {
        return offset;
    }

    public final int getCount() {
        return count;
    }

}
