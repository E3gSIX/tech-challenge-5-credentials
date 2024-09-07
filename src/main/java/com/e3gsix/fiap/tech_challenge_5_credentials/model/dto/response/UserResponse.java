package com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        UserRole role
) {
}
