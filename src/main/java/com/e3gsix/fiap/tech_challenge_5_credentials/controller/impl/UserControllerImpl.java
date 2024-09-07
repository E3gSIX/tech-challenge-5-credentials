package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.UserController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;

@Controller
@RequestMapping(URL_USERS)
public class UserControllerImpl implements UserController {

    public static final String URL_USERS = "/users";
    public static final String URL_USERS_FIND_BY_ID = "/{id}";

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
}
