package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserLoginRequest;

public interface AuthenticationService {
    String login(UserLoginRequest request);
}
