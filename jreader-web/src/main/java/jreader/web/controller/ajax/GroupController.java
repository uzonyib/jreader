package jreader.web.controller.ajax;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jreader.dto.GroupDto;
import jreader.services.GroupService;
import jreader.web.controller.ResponseEntity;

@RestController
@RequestMapping(value = "/reader/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity listAll(final Principal principal) {
        return new ResponseEntity(groupService.list(principal.getName()));
    }

    @PostMapping
    public GroupDto create(final Principal principal, @RequestBody final GroupDto group) {
        return groupService.create(principal.getName(), group.getTitle());
    }

    @PutMapping
    public List<GroupDto> update(final Principal principal, @RequestBody final List<GroupDto> groups) {
        groupService.reorder(principal.getName(), groups);
        return groupService.list(principal.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(final Principal principal, @PathVariable final Long id) {
        groupService.delete(principal.getName(), id);
    }

    @PutMapping("/{id}")
    public GroupDto update(final Principal principal, @PathVariable final Long id, @RequestBody final GroupDto group) {
        group.setId(id);
        return groupService.update(principal.getName(), group);
    }

}
