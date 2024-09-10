package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.UserController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;

@RestController
@RequestMapping(URL_USERS)
public class UserControllerImpl implements UserController {

    public static final String URL_USERS = "/users";
    public static final String URL_USERS_FIND_BY_ID = "/{id}";
    public static final String URL_UPDATE = "/{id}";

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping(URL_USERS_FIND_BY_ID)
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse userFound = this.userService.findById(id);

        return ResponseEntity.ok(userFound);
    }

    @Override
    @PutMapping(URL_UPDATE)
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        UserResponse updatedUser = this.userService.update(id, request);

        return ResponseEntity.ok(updatedUser);
    }
}
