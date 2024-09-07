package com.e3gsix.fiap.tech_challenge_5_credentials.controller;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserLoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationController {

    ResponseEntity register(UserCreateRequest request);

    ResponseEntity login(UserLoginRequest request);

}
