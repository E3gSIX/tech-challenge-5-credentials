package com.e3gsix.fiap.tech_challenge_5_credentials.controller;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserLoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface AuthenticationController {

    ResponseEntity register(UserCreateRequest request, UriComponentsBuilder uriComponentsBuilder);

    ResponseEntity login(UserLoginRequest request);

}
