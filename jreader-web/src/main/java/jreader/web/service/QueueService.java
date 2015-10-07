package jreader.web.service;

public interface QueueService {

    void refresh();
    
    void refresh(String url);

    void cleanup();
    
    void cleanup(String url);

}
