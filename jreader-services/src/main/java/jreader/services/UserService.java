package jreader.services;

public interface UserService {

    void register(String username);

    void ensureIsRegistered(String username);

}
