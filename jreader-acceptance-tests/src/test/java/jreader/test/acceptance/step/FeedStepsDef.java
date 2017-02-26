package jreader.test.acceptance.step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import jreader.feed.server.model.Feed;
import jreader.feed.server.model.Post;

public class FeedStepsDef extends StepDefs {

    private static final String BASE_URL = "http://localhost:8081/feeds";

    private static final Map<Character, Long> OFFSETS = new HashMap<Character, Long>();
    static {
        OFFSETS.put('s', 1000L);
        OFFSETS.put('m', 1000L * 60L);
        OFFSETS.put('h', 1000L * 60L * 60L);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Given("^the following feeds:$")
    public void registerFeeds(DataTable table) {
        restTemplate.delete(BASE_URL);
        for (List<String> row : table.raw()) {
            restTemplate.put(BASE_URL, new Feed(row.get(0), row.get(1)));
        }
    }

    @Given("^the following posts:$")
    public void registerPosts(DataTable table) {
        for (List<String> row : table.raw()) {
            restTemplate.put(BASE_URL + "/" + row.get(0), new Post(row.get(1), row.get(2), row.get(3), row.get(4), getPublishDate(row.get(5))));
        }
    }

    private Long getPublishDate(String age) {
        Character unit = age.charAt(age.length() - 1);
        Long multiplier = Long.valueOf(age.substring(0, age.length() - 1));
        return System.currentTimeMillis() - multiplier * OFFSETS.get(unit);
    }

}
