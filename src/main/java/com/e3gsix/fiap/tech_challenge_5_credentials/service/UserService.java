package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {

    void register(UserCreateRequest request);

    UserResponse findById(UUID id);
}
