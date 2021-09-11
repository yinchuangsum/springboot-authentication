package com.example.springbootloginauthentication.repository;

import com.example.springbootloginauthentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}
