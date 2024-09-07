package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;

public interface UserService {

    void register(UserCreateRequest request);

}
