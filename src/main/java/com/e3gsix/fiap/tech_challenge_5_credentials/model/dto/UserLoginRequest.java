package com.e3gsix.fiap.tech_challenge_5_credentials.model.dto;

public record UserLoginRequest(
        String username,
        String password
) {
}
