package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.NotFoundException;
import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.ResourceAlreadyExistException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UUID register(UserCreateRequest request) {
        if (this.userRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExistException("Username já está em uso.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(request.password());

        User entity = new User(request.username(), encryptedPassword, request.role());

        User savedUsed = this.userRepository.save(entity);

        return  savedUsed.getId();
    }

    @Override
    public UserResponse findById(UUID id) {
        User userFound = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id '" + id + "' não foi encontrado."));

        return new UserResponse(userFound.getId(), userFound.getUsername(), userFound.getRole());
    }
}
