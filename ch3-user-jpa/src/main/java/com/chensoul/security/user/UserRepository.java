package com.chensoul.security.user;

public interface UserRepository {

    User findUserByUsername(String username);
}
