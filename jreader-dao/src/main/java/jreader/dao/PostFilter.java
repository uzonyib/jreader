package jreader.dao;

import lombok.Value;

@Value
public class PostFilter {

    public enum PostType {
        ALL, UNREAD, BOOKMARKED;
    }

    private PostType postType;
    private boolean ascending;
    private int offset;
    private int count;

}
