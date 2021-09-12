package com.example.springbootloginauthentication;

import com.example.springbootloginauthentication.model.Role;
import com.example.springbootloginauthentication.model.User;
import com.example.springbootloginauthentication.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootLoginAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLoginAuthenticationApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            // spring boot requires "ROLE_" prefix by default, look at https://web.archive.org/web/20180201001929/http://forum.spring.io/forum/spring-projects/security/51066-how-to-change-role-from-interceptor-url to modify
            Role admin = userService.saveRole(new Role(null, "ROLE_ADMIN"));
            Role user = userService.saveRole(new Role(null, "ROLE_USER"));

            userService.saveUser(new User(null, "foo", "foo", "foo", "foo", admin.getId()));
            userService.saveUser(new User(null, "bar", "bar", "bar", "bar", user.getId()));
        };
    }

}
