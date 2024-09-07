package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;

public interface TokenService {

    String validateToken(String token);

    String generateToken(User user);
}
