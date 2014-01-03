package jreader.service;

public interface CronService {
	
	void refreshFeeds();
	
	void cleanup(int olderThanDays, int keptCount);

}
