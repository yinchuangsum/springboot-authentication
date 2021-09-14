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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
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

        User user = userService.getUser(authenticationRequest.getUsername());

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @GetMapping(value = "/authenticate/refresh")
    @ResponseBody
    public AuthenticationResponse refreshToken(HttpServletRequest request) throws Exception {
        String username = null;
        String refreshToken = null;
        String accessToken = null;
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            refreshToken = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(refreshToken);
        }

        if (username != null) {
            User user = userService.getUser(username);
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                accessToken = jwtTokenUtil.generateAccessToken(user);
            }
        }

        if (accessToken != null) {
            return new AuthenticationResponse(accessToken, refreshToken);
        }

        throw new RuntimeException("Missing JWT Token");
    }
}
