package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserCreateRequest;

public interface UserService {

    void register(UserCreateRequest request);

}
