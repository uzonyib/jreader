package jreader.web.service.appengine;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import jreader.web.service.QueueService;

public class AppengineQueueService implements QueueService {
    
    @Override
    public void refresh() {
        QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withUrl("/tasks/refresh"));
    }
    
    @Override
    public void refresh(final String url) {
        QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withUrl("/tasks/refresh/feed").param("url", url));
    }
    
    @Override
    public void cleanup() {
        QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withUrl("/tasks/cleanup"));
    }
    
    @Override
    public void cleanup(final String url) {
        QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withUrl("/tasks/cleanup/feed").param("url", url));
    }

}
