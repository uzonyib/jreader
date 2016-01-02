package jreader.services.impl;

import java.util.List;

import jreader.dto.UserDto;

public interface UserAdminService {
    
    List<UserDto> list(int offset, int count);

}
