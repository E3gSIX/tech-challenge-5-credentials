package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserLoginRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserLoginResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.AuthenticationService;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerImplTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationControllerImpl underTest;

    @Test
    void register_ShouldReturnCreatedStatus() {
        //Given
        UserCreateRequest request = new UserCreateRequest("username", "password", UserRole.USER);
        UUID registeredId = UUID.randomUUID();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        when(userService.register(any(UserCreateRequest.class))).thenReturn(registeredId);

        //When
        ResponseEntity<?> response = underTest.register(request, uriComponentsBuilder);

        //Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.created(URI.create("/users/" + registeredId)).build(), response);
        verify(userService, times(1)).register(any(UserCreateRequest.class));
    }

    @Test
    void login_ShouldReturnOkStatusWithToken() {
        //Given
        UserLoginRequest request = new UserLoginRequest("username", "password");
        String token = "mockedToken";

        when(authenticationService.login(any(UserLoginRequest.class))).thenReturn(token);

        //When
        ResponseEntity<?> response = underTest.login(request);

        //Then
        assertEquals(ResponseEntity.ok(new UserLoginResponse(token)), response);
        verify(authenticationService, times(1)).login(any(UserLoginRequest.class));
    }

}