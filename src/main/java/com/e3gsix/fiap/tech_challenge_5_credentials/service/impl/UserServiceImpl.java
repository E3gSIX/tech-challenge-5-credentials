package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.NotFoundException;
import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.ResourceAlreadyExistException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
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

        User entity = new User(request.username(), request.password(), request.role());

        User savedUsed = this.userRepository.save(entity);

        return savedUsed.getId();
    }

    @Override
    public UserResponse findById(UUID id) {
        User userFound = getUser(id);

        return toResponse(userFound);
    }

    @Override
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User entity = getUser(id);

        entity.setUsername(request.username());
        entity.setPassword(request.password());
        entity.setRole(request.role());

        User updatedUser = this.userRepository.save(entity);

        return toResponse(updatedUser);
    }

    private User getUser(UUID id) {
        User userFound = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com id '" + id + "' não foi encontrado."));
        return userFound;
    }

    private UserResponse toResponse(User userFound) {
        return new UserResponse(userFound.getId(), userFound.getUsername(), userFound.getRole());
    }
}
