package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.NotFoundException;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserLoginRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl underTest;

    @Test
    void login_UserExists_AuthenticationSuccessful_ReturnsToken() {
        // Given
        String username = "user";
        String password = "password";
        String token = "jwt-token";

        UserLoginRequest request = new UserLoginRequest(username, password);

        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(tokenService.generateToken(any())).thenReturn(token);

        // When
        String result = underTest.login(request);

        // Then
        assertEquals(token, result);
        verify(userRepository).existsByUsername(username);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(any());
    }

    @Test
    void login_UserDoesNotExist_ThrowsNotFoundException() {
        // Given
        String username = "nonexistentuser";
        UserLoginRequest request = new UserLoginRequest(username, "password");

        when(userRepository.existsByUsername(username)).thenReturn(false);

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            underTest.login(request)
        );

        // Then
        assertEquals("Usuário '" + username + "' não foi encontrado.", exception.getMessage());
        verify(userRepository).existsByUsername(username);
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(tokenService);
    }

}