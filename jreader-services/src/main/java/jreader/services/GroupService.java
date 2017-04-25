package jreader.services;

import java.util.List;

import jreader.dto.GroupDto;

public interface GroupService {

    List<GroupDto> list(String username);

    GroupDto create(String username, String title);

    GroupDto update(String username, GroupDto group);

    void reorder(String username, List<GroupDto> groups);

    void entitle(String username, Long groupId, String title);

    void delete(String username, Long groupId);

}
