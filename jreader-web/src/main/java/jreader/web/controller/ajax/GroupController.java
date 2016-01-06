package jreader.web.controller.ajax;

import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jreader.services.GroupService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/reader/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listAll(final Principal principal) {
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity create(final Principal principal, @RequestParam final String title) {
        groupService.create(principal.getName(), title);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(final Principal principal, @PathVariable final Long groupId) {
        groupService.delete(principal.getName(), groupId);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}/title", method = RequestMethod.PUT)
    public ResponseEntity entitle(final Principal principal, @PathVariable final Long groupId, @RequestParam final String value) {
        groupService.entitle(principal.getName(), groupId, value);
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @RequestMapping(value = "/{groupId}/order", method = RequestMethod.PUT)
    public ResponseEntity move(final Principal principal, @PathVariable final Long groupId, @RequestParam final boolean up) {
        if (up) {
            groupService.moveUp(principal.getName(), groupId);
        } else {
            groupService.moveDown(principal.getName(), groupId);
        }

        return new ResponseEntity(groupService.list(principal.getName()));
    }

}
