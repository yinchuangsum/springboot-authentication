package com.example.springbootloginauthentication.controller;

import com.example.springbootloginauthentication.model.User;
import com.example.springbootloginauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/user")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
}
