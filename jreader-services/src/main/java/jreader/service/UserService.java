package jreader.service;

import jreader.dto.UserDto;

public interface UserService {
	
	UserDto getUser(String username);
	
	void register(String username);

}
