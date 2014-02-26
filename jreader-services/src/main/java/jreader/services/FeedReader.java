package jreader.services;

import java.net.URL;

import jreader.dto.RssFetchResult;

public interface FeedReader {
	
	RssFetchResult fetch(URL url) throws Exception;

}
