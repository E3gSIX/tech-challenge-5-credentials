package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.ResourceAlreadyExistException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void register(UserCreateRequest request) {
        if (this.userRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExistException("Username já está em uso.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());

        User entity = new User(request.username(), encryptedPassword, request.role());

        this.userRepository.save(entity);
    }

}
