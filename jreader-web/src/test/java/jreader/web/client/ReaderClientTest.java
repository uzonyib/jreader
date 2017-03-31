package jreader.web.client;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@RestClientTest(ReaderClient.class)
@ContextConfiguration(classes = ClientConfiguration.class)
public class ReaderClientTest {

    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReaderClient sut;

    @Test
    public void createGroupShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/reader/groups?title=group")).andExpect(method(HttpMethod.POST)).andRespond(withSuccess());

        sut.createGroup("group");

        server.verify();
    }

    @Test
    public void deleteGroupShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/reader/groups/1")).andExpect(method(HttpMethod.DELETE)).andRespond(withSuccess());

        sut.deleteGroup(1L);

        server.verify();
    }

    @Test
    public void renameGroupShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/reader/groups/1/title?value=title")).andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess());

        sut.renameGroup(1L, "title");

        server.verify();
    }

    @Test
    public void moveGroupUpShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/reader/groups/1/order?up=true")).andExpect(method(HttpMethod.PUT)).andRespond(withSuccess());

        sut.moveGroupUp(1L);

        server.verify();
    }

    @Test
    public void moveGroupDownShouldMakeProperRequest() {
        server.expect(requestTo(ClientConfiguration.BASE_URL + "/reader/groups/1/order?up=false")).andExpect(method(HttpMethod.PUT)).andRespond(withSuccess());

        sut.moveGroupDown(1L);

        server.verify();
    }

}
