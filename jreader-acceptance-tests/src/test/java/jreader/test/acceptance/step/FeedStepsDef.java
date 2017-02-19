package jreader.test.acceptance.step;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import jreader.feed.server.model.Feed;

public class FeedStepsDef extends StepDefs {

    private static final String BASE_URL = "http://localhost:8081/feeds";

    @Autowired
    private RestTemplate restTemplate;

    @Given("^the following feeds:$")
    public void registerFeeds(DataTable table) {
        restTemplate.delete(BASE_URL);
        for (List<String> row : table.raw()) {
            restTemplate.put(BASE_URL, new Feed(row.get(0), row.get(1)));
        }
    }

}
