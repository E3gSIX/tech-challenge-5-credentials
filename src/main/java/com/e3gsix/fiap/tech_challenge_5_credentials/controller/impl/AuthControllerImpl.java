package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.AuthController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.AuthControllerImpl.URL_AUTH;

@RestController
@RequestMapping(URL_AUTH)
public class AuthControllerImpl implements AuthController {

    public static final String URL_AUTH = "/auth";
    public static final String URl_REGISTER = "/register";

    private final UserService userService;

    public AuthControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PostMapping(URl_REGISTER)
    public ResponseEntity register(@RequestBody UserCreateRequest request) {
        this.userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
