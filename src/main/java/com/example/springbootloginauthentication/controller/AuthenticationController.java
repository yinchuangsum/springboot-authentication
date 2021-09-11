package com.example.springbootloginauthentication.controller;

import com.example.springbootloginauthentication.model.User;
import com.example.springbootloginauthentication.model.auth.AuthenticationRequest;
import com.example.springbootloginauthentication.model.auth.AuthenticationResponse;
import com.example.springbootloginauthentication.security.JwtUtil;
import com.example.springbootloginauthentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping(value = "/authenticate")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final User user = userService.getUser(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(user);

        return new AuthenticationResponse(jwt);

    }
}
