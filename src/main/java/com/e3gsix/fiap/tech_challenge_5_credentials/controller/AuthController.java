package com.e3gsix.fiap.tech_challenge_5_credentials.controller;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserCreateRequest;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity register(UserCreateRequest request);

}
