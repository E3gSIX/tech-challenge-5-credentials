package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.NotFoundException;
import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.ResourceAlreadyExistException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl underTest;

    @Test
    void register_ShouldThrowResourceAlreadyExistException_WhenUsernameExists() {
        UserCreateRequest request = new UserCreateRequest("existingUser", "password", UserRole.USER);
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> underTest.register(request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ShouldReturnUserId_WhenUserIsSuccessfullyRegistered() {
        UserCreateRequest request = new UserCreateRequest("newUser", "password", UserRole.USER);
        User user = new User(request.username(), request.password(), request.role());
        user.setId(UUID.randomUUID());

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UUID userId = underTest.register(request);

        assertNotNull(userId);
        assertEquals(user.getId(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findById_ShouldReturnUserResponse_WhenUserExists() {
        UUID userId = UUID.randomUUID();
        User user = new User("username", "password", UserRole.USER);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = underTest.findById(userId);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals(user.getUsername(), response.username());
        assertEquals(user.getRole(), response.role());
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> underTest.findById(userId));
    }

    @Test
    void update_ShouldReturnUpdatedUserResponse_WhenUserIsSuccessfullyUpdated() {
        UUID userId = UUID.randomUUID();
        User user = new User("username", "password", UserRole.USER);
        user.setId(userId);
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "newPassword", UserRole.ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = underTest.update(userId, request);

        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals(request.username(), response.username());
        assertEquals(request.role(), response.role());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("updatedUser", "newPassword", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> underTest.update(userId, request));
        verify(userRepository, never()).save(any(User.class));
    }

}