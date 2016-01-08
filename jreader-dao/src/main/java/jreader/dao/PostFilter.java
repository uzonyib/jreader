package jreader.dao;

public class PostFilter {

    public static enum PostType {
        ALL, UNREAD, BOOKMARKED;
    }

    private PostType postType;
    private boolean ascending;
    private int offset;
    private int count;

    public PostFilter(final PostType postType, final boolean ascending, final int offset, final int count) {
        this.postType = postType;
        this.ascending = ascending;
        this.offset = offset;
        this.count = count;
    }

    public final PostType getPostType() {
        return postType;
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
