package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityAuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityAuthServiceImpl underTest;

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Given
        String username = "testuser";
        UserDetails expectedUserDetails = mock(UserDetails.class);
        when(userRepository.findByUsername(username)).thenReturn(expectedUserDetails);

        // When
        UserDetails actualUserDetails = underTest.loadUserByUsername(username);

        // Then
        assertEquals(expectedUserDetails, actualUserDetails);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Given
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        // When
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            underTest.loadUserByUsername(username);
        });

        // Then
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}