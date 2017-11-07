package jreader.web.controller.ajax;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import jreader.dao.PostFilter.PostType;
import jreader.services.GroupService;
import jreader.services.PostFilter;
import jreader.services.PostService;
import jreader.services.exception.ResourceNotFoundException;

@WebMvcTest(PostController.class)
public class PostControllerTest extends ControllerTest {

    private static final PostType POST_TYPE = PostType.ALL;
    private static final boolean ASCENDING = true;
    private static final int OFFSET = 0;
    private static final int COUNT = 10;

    @MockBean
    private GroupService groupService;
    @MockBean
    private PostService postService;

    @Test
    public void listForUser() throws Exception {
        final PostFilter filter = new PostFilter(USERNAME, POST_TYPE, ASCENDING, OFFSET, COUNT);
        given(postService.list(filter)).willReturn(Collections.emptyList());

        mvc.perform(buildPostRequest("/reader/posts")).andExpect(status().isOk());
    }

    @Test
    public void listForUserShouldReturnErrorWhenServiceThrowsNotFoundException() throws Exception {
        final PostFilter filter = new PostFilter(USERNAME, POST_TYPE, ASCENDING, OFFSET, COUNT);
        given(postService.list(filter)).willThrow(new ResourceNotFoundException("user not found"));

        mvc.perform(buildPostRequest("/reader/posts")).andExpect(status().isNotFound());
    }

    @Test
    public void listForGroupShouldReturnErrorWhenServiceThrowsNotFoundException() throws Exception {
        final PostFilter filter = new PostFilter(USERNAME, 1L, POST_TYPE, ASCENDING, OFFSET, COUNT);
        given(postService.list(filter)).willThrow(new ResourceNotFoundException("group not found"));

        mvc.perform(buildPostRequest("/reader/groups/1/posts")).andExpect(status().isNotFound());
    }

    @Test
    public void listForSubscriptionShouldReturnErrorWhenServiceThrowsNotFoundException() throws Exception {
        final PostFilter filter = new PostFilter(USERNAME, 1L, 2L, POST_TYPE, ASCENDING, OFFSET, COUNT);
        given(postService.list(filter)).willThrow(new ResourceNotFoundException("subscription not found"));

        mvc.perform(buildPostRequest("/reader/groups/1/subscriptions/2/posts")).andExpect(status().isNotFound());
    }

    private MockHttpServletRequestBuilder buildPostRequest(String baseUri) {
        return get(baseUri + "/" + POST_TYPE.name().toLowerCase()).param("offset", String.valueOf(OFFSET)).param("count", String.valueOf(COUNT))
                .param("ascending", String.valueOf(ASCENDING)).principal(principal);
    }

}
