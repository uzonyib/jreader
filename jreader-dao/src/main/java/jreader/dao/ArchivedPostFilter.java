package jreader.dao;

import lombok.Value;

@Value
public class ArchivedPostFilter {

    private boolean ascending;
    private int offset;
    private int count;

}
