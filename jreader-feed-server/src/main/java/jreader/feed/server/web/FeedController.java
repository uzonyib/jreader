package jreader.feed.server.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;

import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;
import jreader.feed.server.service.FeedRegistry;

@RestController
public class FeedController {

    @Autowired
    private FeedRegistry registry;

    @Autowired
    private ConversionService converter;

    @Value("${feed-server.base-url}")
    private String baseUrl;

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

    @RequestMapping("/feeds/{feed}/rss")
    public String listRssPosts(@PathVariable final String feed) throws FeedException {
        final SyndFeed syndFeed = converter.convert(registry.getFeed(feed), SyndFeed.class);
        syndFeed.setFeedType("rss_2.0");
        syndFeed.setLink(baseUrl + "/feeds/" + feed + "/rss");
        syndFeed.setEntries(new ArrayList<SyndEntry>());

        final List<Post> posts = registry.listPosts(feed);
        for (final Post post : posts) {
            final SyndEntry syndEntry = converter.convert(post, SyndEntry.class);
            syndEntry.setLink(baseUrl + "/feeds/" + feed + "/rss/" + post.getUrl());
            syndFeed.getEntries().add(syndEntry);
        }

        return new SyndFeedOutput().outputString(syndFeed);
    }

    @RequestMapping(value = "/feeds/{feed}", method = RequestMethod.PUT)
    public void addPost(@PathVariable final String feed, @RequestBody final Post post) {
        registry.add(feed, post);
    }

}
