package jreader.services;

import jreader.domain.Role;

public interface UserService {

    boolean isRegistered(String username);
    
    void register(String username, Role role);

    Role getRole(String username);

}
