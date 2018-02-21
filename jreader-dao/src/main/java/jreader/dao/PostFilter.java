package jreader.dao;

import org.springframework.data.domain.Pageable;

import lombok.Value;

@Value
public class PostFilter {

    public enum PostType {
        ALL, UNREAD, BOOKMARKED;
    }

    private PostType postType;
    private Pageable page;

}
