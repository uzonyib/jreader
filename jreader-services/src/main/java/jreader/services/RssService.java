package jreader.services;

import jreader.dto.RssFetchResult;

public interface RssService {

    RssFetchResult fetch(String url);

}
