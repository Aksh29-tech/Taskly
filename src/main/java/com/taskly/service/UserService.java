package com.taskly.service;

import com.taskly.entity.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
}
