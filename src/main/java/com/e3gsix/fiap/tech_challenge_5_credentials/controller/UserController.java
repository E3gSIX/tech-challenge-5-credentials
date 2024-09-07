package com.e3gsix.fiap.tech_challenge_5_credentials.controller;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserController {

    ResponseEntity<UserResponse> findById(UUID id);

    ResponseEntity<UserResponse> update(UUID id, UserUpdateRequest request);

}
