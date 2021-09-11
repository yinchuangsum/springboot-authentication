package com.example.springbootloginauthentication.service;

import com.example.springbootloginauthentication.model.Role;
import com.example.springbootloginauthentication.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    Role getRole(String name);
    User getUser(String username);
    // can do pagination
    List<User> getUsers();
}
