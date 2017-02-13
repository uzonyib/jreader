package jreader.feed.server.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;
import jreader.feed.server.service.FeedRegistry;

@RestController
public class FeedController {

    @Autowired
    private FeedRegistry registry;

    @RequestMapping(value = "/feeds", method = RequestMethod.DELETE)
    public void reset() {
        registry.reset();
    }

    @RequestMapping("/feeds")
    public List<Feed> listFeeds() {
        return registry.listFeeds();
    }

    @RequestMapping(value = "/feeds", method = RequestMethod.PUT)
    public void addFeed(@RequestBody final Feed feed) {
        registry.add(feed);
    }

    @RequestMapping("/feeds/{feed}")
    public List<Post> listPosts(@PathVariable final String feed) {
        return registry.listPosts(feed);
    }

    @RequestMapping(value = "/feeds/{feed}", method = RequestMethod.PUT)
    public void addPost(@PathVariable final String feed, @RequestBody final Post post) {
        registry.add(feed, post);
    }

}
