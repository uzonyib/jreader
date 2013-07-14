package jreader.rss;

import jreader.rss.domain.Feed;

public interface RssService {
	
	Feed fetch(String url);

}
