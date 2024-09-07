package com.e3gsix.fiap.tech_challenge_5_credentials.model.dto;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;

public record UserCreateRequest(
        String username,
        String password,
        UserRole role
) {
}
