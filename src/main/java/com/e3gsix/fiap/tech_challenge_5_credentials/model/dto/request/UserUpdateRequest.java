package com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;

public record UserUpdateRequest(
        String username,
        String password,
        UserRole role
) {
}
