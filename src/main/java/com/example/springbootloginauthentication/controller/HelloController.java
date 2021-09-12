package com.example.springbootloginauthentication.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HelloController {

    @PreAuthorize("permitAll()")
    @RequestMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/helloadmin")
    public String sayHelloAdmin() {
        return "hello";
    }
}
