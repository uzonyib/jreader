package jreader.feed.server.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockMvc.perform(delete("/feeds")).andExpect(status().isOk());
    }

    @Test
    public void listFeedsShouldReturnEmptyResultByDefault() throws Exception {
        mockMvc.perform(get("/feeds")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void addFeedShouldCreateFeed() throws Exception {
        final Feed feed1 = new Feed("Movies", "News about films, starts, etc.");
        final Feed feed2 = new Feed("Books", "Best book reviews");

        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(feed1))).andExpect(status().isOk());
        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(feed2))).andExpect(status().isOk());

        mockMvc.perform(get("/feeds")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo(feed1.getTitle())))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo(feed1.getDescription())))
                .andExpect(jsonPath("$[1].title", Matchers.equalTo(feed2.getTitle())))
                .andExpect(jsonPath("$[1].description", Matchers.equalTo(feed2.getDescription())));

        mockMvc.perform(get("/feeds/" + feed1.getTitle() + "/rss")).andExpect(status().isOk())
                .andExpect(xpath("/rss/channel/title").string(Matchers.equalTo(feed1.getTitle())))
                .andExpect(xpath("/rss/channel/description").string(Matchers.equalTo(feed1.getDescription())))
                .andExpect(xpath("/rss/channel/link").string(Matchers.notNullValue()));
    }

    @Test
    public void addPostShouldCreatePostInFeed() throws Exception {
        final Feed feed = new Feed("Cars", "Awesome cars");
        final Post post1 = new Post("Le Mans results", "The La Mans results are...", "f1lover", "le-mans-results.html", null);
        final Post post2 = new Post("F1 - Fastest laps", "Fastest lap times by driver", "f1fanatic", "fastest-laps.html", null);

        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(feed))).andExpect(status().isOk());
        mockMvc.perform(put("/feeds/" + feed.getTitle()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(post1)))
                .andExpect(status().isOk());
        mockMvc.perform(put("/feeds/" + feed.getTitle()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(post2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/feeds/" + feed.getTitle())).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo(post1.getTitle())))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo(post1.getDescription())))
                .andExpect(jsonPath("$[0].author", Matchers.equalTo(post1.getAuthor())))
                .andExpect(jsonPath("$[0].url", Matchers.equalTo(post1.getUrl())))
                .andExpect(jsonPath("$[0].publishDate", Matchers.notNullValue()))
                .andExpect(jsonPath("$[1].title", Matchers.equalTo(post2.getTitle())))
                .andExpect(jsonPath("$[1].description", Matchers.equalTo(post2.getDescription())))
                .andExpect(jsonPath("$[1].author", Matchers.equalTo(post2.getAuthor())))
                .andExpect(jsonPath("$[1].url", Matchers.equalTo(post2.getUrl())))
                .andExpect(jsonPath("$[1].publishDate", Matchers.notNullValue()));

        mockMvc.perform(get("/feeds/" + feed.getTitle() + "/rss")).andExpect(status().isOk())
                .andExpect(xpath("/rss/channel/item[1]/title").string(Matchers.equalTo(post1.getTitle())))
                .andExpect(xpath("/rss/channel/item[1]/description").string(Matchers.equalTo(post1.getDescription())))
                .andExpect(xpath("/rss/channel/item[1]/creator").string(Matchers.equalTo(post1.getAuthor())))
                .andExpect(xpath("/rss/channel/item[1]/guid").string(Matchers.notNullValue()))
                .andExpect(xpath("/rss/channel/item[1]/pubDate").string(Matchers.notNullValue()))
                .andExpect(xpath("/rss/channel/item[2]/title").string(Matchers.equalTo(post2.getTitle())))
                .andExpect(xpath("/rss/channel/item[2]/description").string(Matchers.equalTo(post2.getDescription())))
                .andExpect(xpath("/rss/channel/item[2]/creator").string(Matchers.equalTo(post2.getAuthor())))
                .andExpect(xpath("/rss/channel/item[2]/guid").string(Matchers.notNullValue()))
                .andExpect(xpath("/rss/channel/item[2]/pubDate").string(Matchers.notNullValue()));
    }

}
