package jreader.feed.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import jreader.feed.client.FeedApiClient;
import jreader.feed.model.Feed;
import jreader.feed.model.Post;

@RunWith(SpringRunner.class)
@RestClientTest(FeedApiClient.class)
@ContextConfiguration(classes = ClientConfiguration.class)
public class FeedApiClientTest {

    private static final Feed FEED_2 = new Feed("feed2", "desc2");
    private static final Feed FEED_1 = new Feed("feed1", "desc1");
    private static final List<Feed> FEEDS = Arrays.asList(FEED_1, FEED_2);
    private static final Post POST_2 = new Post("title2", "desc2", "author2", "url2", 2000L);
    private static final Post POST_1 = new Post("title1", "desc1", "author1", "url1", 1000L);
    private static final List<Post> POSTS = Arrays.asList(POST_1, POST_2);

    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FeedApiClient sut;

    @Test
    public void deleteAllFeedsShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds")).andExpect(method(HttpMethod.DELETE)).andRespond(withSuccess());

        sut.deleteAllFeeds();

        server.verify();
    }

    @Test
    public void listFeedsShouldMakeProperRequest() throws Exception {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds")).andRespond(withSuccess(mapper.writeValueAsString(FEEDS), MediaType.APPLICATION_JSON));

        final List<Feed> actual = sut.listFeeds();

        server.verify();
        assertThat(actual).isEqualTo(FEEDS);
    }

    @Test
    public void addFeedsShouldMakeProperRequest() throws Exception {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds")).andExpect(method(HttpMethod.PUT))
                .andExpect(content().string(mapper.writeValueAsString(FEED_1))).andRespond(withSuccess());

        sut.addFeed(FEED_1);

        server.verify();
    }

    @Test
    public void listPostsShouldMakeProperRequest() throws Exception {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds/" + FEED_1.getTitle()))
                .andRespond(withSuccess(mapper.writeValueAsString(POSTS), MediaType.APPLICATION_JSON));

        final List<Post> actual = sut.listPosts(FEED_1.getTitle());

        server.verify();
        assertThat(actual).isEqualTo(POSTS);
    }

    @Test
    public void addPostShouldMakeProperRequest() throws Exception {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds/" + FEED_1.getTitle())).andExpect(method(HttpMethod.PUT))
                .andExpect(content().string(mapper.writeValueAsString(POST_1))).andRespond(withSuccess());

        sut.addPost(FEED_1.getTitle(), POST_1);

        server.verify();
    }

    @Test
    public void listRssPostsShouldMakeProperRequest() throws Exception {
        final String rssResponse = "<rss></rss>";
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/feeds/" + FEED_1.getTitle() + "/rss"))
                .andRespond(withSuccess(rssResponse, MediaType.APPLICATION_XML));

        final String actual = sut.listRssPosts(FEED_1.getTitle());

        server.verify();
        assertThat(actual).isEqualTo(rssResponse);
    }

}
