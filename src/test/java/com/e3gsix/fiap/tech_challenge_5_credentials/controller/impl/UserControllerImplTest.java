package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl underTest;

    @Test
    void testFindById() {
        // Given
        UUID userId = UUID.fromString("df14d9f7-fe32-470c-8174-17ce860300d4");
        UserResponse userResponse =
                new UserResponse(userId,
                "username",
                UserRole.USER);
        when(userService.findById(userId)).thenReturn(userResponse);

        // When
        ResponseEntity<UserResponse> response = underTest.findById(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testUpdate() {
        // Given
        UUID userId = UUID.fromString("df14d9f7-fe32-470c-8174-17ce860300d4");
        UserUpdateRequest updateRequest = new UserUpdateRequest("username",
                "password",
                UserRole.USER);
        UserResponse updatedUserResponse = new UserResponse(userId,
                "username",
                UserRole.USER);
        when(userService.update(eq(userId), any(UserUpdateRequest.class))).thenReturn(updatedUserResponse);

        // When
        ResponseEntity<UserResponse> response = underTest.update(userId, updateRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserResponse, response.getBody());
        verify(userService, times(1)).update(eq(userId), eq(updateRequest));
    }

}