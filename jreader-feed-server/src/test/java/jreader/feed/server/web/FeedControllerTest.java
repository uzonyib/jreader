package jreader.feed.server.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
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
    public void listFeeds() throws Exception {
        mockMvc.perform(get("/feeds")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void testFeedCreation() throws Exception {
        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Feed("Movies")))).andExpect(status().isOk());
        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Feed("Books")))).andExpect(status().isOk());
        
        mockMvc.perform(get("/feeds")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo("Movies")))
                .andExpect(jsonPath("$[1].title", Matchers.equalTo("Books")));
    }

    @Test
    public void testPostCreation() throws Exception {
        mockMvc.perform(put("/feeds").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new Feed("Cars")))).andExpect(status().isOk());
        mockMvc.perform(get("/feeds")).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo("Cars")));

        final Post post1 = new Post("Le Mans results", "The La Mans results are...", "f1lover", "le-mans-results.html");
        final Post post2 = new Post("F1 - Fastest laps", "Fastest lap times by driver", "f1fanatic", "fastest-laps.html");
        mockMvc.perform(put("/feeds/Cars").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(post1))).andExpect(status().isOk());
        mockMvc.perform(put("/feeds/Cars").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(post2))).andExpect(status().isOk());

        mockMvc.perform(get("/feeds/Cars")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title", Matchers.equalTo(post1.getTitle())))
                .andExpect(jsonPath("$[0].description", Matchers.equalTo(post1.getDescription())))
                .andExpect(jsonPath("$[0].author", Matchers.equalTo(post1.getAuthor())))
                .andExpect(jsonPath("$[0].url", Matchers.equalTo(post1.getUrl())))
                .andExpect(jsonPath("$[1].title", Matchers.equalTo(post2.getTitle())))
                .andExpect(jsonPath("$[1].description", Matchers.equalTo(post2.getDescription())))
                .andExpect(jsonPath("$[1].author", Matchers.equalTo(post2.getAuthor())))
                .andExpect(jsonPath("$[1].url", Matchers.equalTo(post2.getUrl())));
    }

}
