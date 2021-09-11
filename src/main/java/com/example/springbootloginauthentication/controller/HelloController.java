package com.example.springbootloginauthentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HelloController {

    @RequestMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}
