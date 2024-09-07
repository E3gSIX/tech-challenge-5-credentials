package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.NotFoundException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserLoginRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.AuthenticationService;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            TokenService tokenService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(UserLoginRequest request) {
        if(!this.userRepository.existsByUsername(request.username())) {
            throw new NotFoundException("Usuário '" + request.username() + "' não foi encontrado.");
        }

        var loginAuth = new UsernamePasswordAuthenticationToken(request.username(), request.password());

        Authentication authentication = authenticationManager.authenticate(loginAuth);

        return tokenService.generateToken((User) authentication.getPrincipal());
    }
}
