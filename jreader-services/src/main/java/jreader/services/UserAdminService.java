package jreader.services;

import java.util.List;

import jreader.dto.UserDto;

public interface UserAdminService {
    
    List<UserDto> list(int offset, int count);
    
    void updateRole(String username, String role);

}
