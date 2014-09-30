package jreader.dao;

public class FeedEntryFilter {

    public static enum Selection {
        ALL, UNREAD, STARRED;
    }

    protected Selection selection;
    protected boolean ascending;
    protected int offset;
    protected int count;

    public FeedEntryFilter(final Selection selection, final boolean ascending, final int offset, final int count) {
        this.selection = selection;
        this.ascending = ascending;
        this.offset = offset;
        this.count = count;
    }

    public final Selection getSelection() {
        return selection;
    }

    public final void setSelection(final Selection selection) {
        this.selection = selection;
    }

    public final boolean isAscending() {
        return ascending;
    }

    public final void setAscending(final boolean ascending) {
        this.ascending = ascending;
    }

    public final int getOffset() {
        return offset;
    }

    public final void setOffset(final int offset) {
        this.offset = offset;
    }

    public final int getCount() {
        return count;
    }

    public final void setCount(final int count) {
        this.count = count;
    }

}
