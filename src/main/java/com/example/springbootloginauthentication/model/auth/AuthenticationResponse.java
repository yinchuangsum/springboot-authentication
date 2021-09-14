package com.example.springbootloginauthentication.model.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private final String accessToken;
    private final String refreshToken;
}
