package jreader.web.service.impl;

import static com.google.appengine.api.taskqueue.QueueFactory.getDefaultQueue;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import org.springframework.stereotype.Service;

import jreader.web.service.QueueService;

@Service
public class AppengineQueueService implements QueueService {

    @Override
    public void refresh() {
        getDefaultQueue().add(withUrl("/tasks/refresh"));
    }

    @Override
    public void refresh(final String url) {
        getDefaultQueue().add(withUrl("/tasks/refresh/feed").param("url", url));
    }

    @Override
    public void cleanup() {
        getDefaultQueue().add(withUrl("/tasks/cleanup"));
    }

    @Override
    public void cleanup(final String url) {
        getDefaultQueue().add(withUrl("/tasks/cleanup/feed").param("url", url));
    }

}
