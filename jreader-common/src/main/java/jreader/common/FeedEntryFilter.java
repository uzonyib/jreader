package jreader.common;

public class FeedEntryFilter {
	
	public static enum Selection {
		ALL, UNREAD, STARRED;
	}
	
	protected Selection selection;
	protected boolean ascending;
	protected int offset;
	protected int count;

	public FeedEntryFilter(Selection selection, boolean ascending, int offset, int count) {
		super();
		this.selection = selection;
		this.ascending = ascending;
		this.offset = offset;
		this.count = count;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
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
