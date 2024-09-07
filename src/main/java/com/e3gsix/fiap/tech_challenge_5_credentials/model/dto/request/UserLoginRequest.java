package com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request;

public record UserLoginRequest(
        String username,
        String password
) {
}
