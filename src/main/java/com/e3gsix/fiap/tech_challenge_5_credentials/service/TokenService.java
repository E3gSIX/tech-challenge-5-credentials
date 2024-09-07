package com.e3gsix.fiap.tech_challenge_5_credentials.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;

public interface TokenService {

    DecodedJWT validateToken(String token);

    String generateToken(User user);
}
