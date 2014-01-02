package jreader.common;

public class FeedEntryFilter {
	
	public static enum Selection {
		ALL, UNREAD, STARRED;
	}
	
	protected Selection selection;
	protected boolean ascending;

	public FeedEntryFilter(Selection selection, boolean ascending) {
		this.selection = selection;
		this.ascending = ascending;
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

}
