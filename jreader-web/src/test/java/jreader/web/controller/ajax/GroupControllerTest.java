package jreader.web.controller.ajax;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import jreader.dto.GroupDto;
import jreader.services.GroupService;
import jreader.services.exception.ResourceAlreadyExistsException;
import jreader.services.exception.ResourceNotFoundException;

@WebMvcTest(GroupController.class)
public class GroupControllerTest extends ControllerTest {

    private static final Long ID = 1L;
    private static final String TITLE = "group1";
    private static final int ORDER = 12;
    private static final int UNREAD_COUNT = 34;
    private static final GroupDto NEW_GROUP = GroupDto.builder()
            .title(TITLE)
            .build();
    private static final GroupDto EXISTING_GROUP = GroupDto.builder()
            .id(ID)
            .title(TITLE)
            .order(ORDER)
            .unreadCount(UNREAD_COUNT)
            .build();
    private static final GroupDto GROUP_TO_UPDATE = GroupDto.builder()
            .title(TITLE)
            .order(ORDER)
            .unreadCount(UNREAD_COUNT)
            .build();

    @MockBean
    private GroupService groupService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void listAllShouldReturnErrorWhenUserIsNotFound() throws Exception {
        given(groupService.list(USERNAME)).willThrow(new ResourceNotFoundException());

        mvc.perform(get("/reader/groups").principal(principal))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContent() throws Exception {
        mvc.perform(delete("/reader/groups/" + ID).principal(principal))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnErrorWhenGroupIsNotFound() throws Exception {
        willThrow(new ResourceNotFoundException("group not found")).given(groupService).delete(USERNAME, ID);

        mvc.perform(delete("/reader/groups/" + ID).principal(principal))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createShouldReturnCreatedGroup() throws Exception {
        given(groupService.create(USERNAME, TITLE)).willReturn(EXISTING_GROUP);

        mvc.perform(post("/reader/groups").principal(principal).content(mapper.writeValueAsString(NEW_GROUP)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.order", is(ORDER)))
                .andExpect(jsonPath("$.unreadCount", is(UNREAD_COUNT)));
    }

    @Test
    public void createShouldReturnErrorWhenTitleIsInvalid() throws Exception {
        given(groupService.create(USERNAME, TITLE)).willThrow(new IllegalArgumentException("invalid title"));

        mvc.perform(post("/reader/groups").principal(principal).content(mapper.writeValueAsString(NEW_GROUP)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createShouldReturnErrorWhenGroupAlreadyExists() throws Exception {
        given(groupService.create(USERNAME, TITLE)).willThrow(new ResourceAlreadyExistsException());

        mvc.perform(post("/reader/groups").principal(principal).content(mapper.writeValueAsString(NEW_GROUP)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void createShouldReturnErrorWhenServiceFails() throws Exception {
        given(groupService.create(USERNAME, TITLE)).willThrow(new RuntimeException());

        mvc.perform(post("/reader/groups").principal(principal).content(mapper.writeValueAsString(NEW_GROUP)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void updateShouldReturnUpdatedGroup() throws Exception {
        given(groupService.update(USERNAME, EXISTING_GROUP)).willReturn(EXISTING_GROUP);

        mvc.perform(put("/reader/groups/" + ID).principal(principal).content(mapper.writeValueAsString(GROUP_TO_UPDATE)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ID.intValue())))
                .andExpect(jsonPath("$.title", is(TITLE)))
                .andExpect(jsonPath("$.order", is(ORDER)))
                .andExpect(jsonPath("$.unreadCount", is(UNREAD_COUNT)));
    }

    @Test
    public void updateShouldReturnErrorWhenGroupAlreadyExists() throws Exception {
        given(groupService.update(USERNAME, EXISTING_GROUP)).willThrow(new ResourceAlreadyExistsException());

        mvc.perform(put("/reader/groups/" + ID).principal(principal).content(mapper.writeValueAsString(GROUP_TO_UPDATE)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

}
