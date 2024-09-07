package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.AuthenticationController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserLoginRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserLoginResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.AuthenticationService;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.AuthenticationControllerImpl.URL_AUTH;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS_FIND_BY_ID;

@RestController
@RequestMapping(URL_AUTH)
public class AuthenticationControllerImpl implements AuthenticationController {

    public static final String URL_AUTH = "/auth";
    public static final String URl_REGISTER = "/register";
    public static final String URl_LOGIN = "/login";

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationControllerImpl(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    @PostMapping(URl_REGISTER)
    public ResponseEntity register(
            @RequestBody UserCreateRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UUID registeredId = this.userService.register(request);

        URI uri = uriComponentsBuilder.path(URL_USERS.concat(URL_USERS_FIND_BY_ID))
                .buildAndExpand(registeredId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Override
    @PostMapping(URl_LOGIN)
    public ResponseEntity login(@RequestBody UserLoginRequest request) {
        String token = this.authenticationService.login(request);

        return ResponseEntity.ok(new UserLoginResponse(token));
    }
}
