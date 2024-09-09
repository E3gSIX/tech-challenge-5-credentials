package com.e3gsix.fiap.tech_challenge_5_credentials.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private TokenServiceImpl underTest;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(underTest, "secret", "test-secret");
    }

    @Test
    public void testGenerateToken_Success() {
        // Given
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");
        doAnswer((Answer<Collection<? extends GrantedAuthority>>) invocation ->
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ).when(user).getAuthorities();

        // When
        String token = underTest.generateToken(user);

        // Then
        assertNotNull(token);
        DecodedJWT decodedJWT = JWT.decode(token);
        assertEquals("auth-api", decodedJWT.getIssuer());
        assertEquals("testUser", decodedJWT.getSubject());
        assertEquals(Collections.singletonList("ROLE_USER"), decodedJWT.getClaim("authorities").asList(String.class));
        assertTrue(decodedJWT.getExpiresAt().toInstant().isAfter(Instant.now()));
    }

    @Test
    public void testGenerateToken_JWTCreationException() {
        // Given
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("testUser");
        doAnswer((Answer<Collection<? extends GrantedAuthority>>) invocation ->
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        ).when(user).getAuthorities();

        when(user.getUsername()).thenThrow(new RuntimeException("Erro ao gerar token."));

        // When
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            underTest.generateToken(user);
        });

        // Then
        assertEquals("Erro ao gerar token.", exception.getMessage());
    }

    @Test
    public void testValidateToken_Success() {
        // Given
        String token = JWT.create()
                .withIssuer("auth-api")
                .withSubject("testUser")
                .sign(Algorithm.HMAC256("test-secret"));

        // When
        DecodedJWT decodedJWT = underTest.validateToken(token);

        // Then
        assertNotNull(decodedJWT);
        assertEquals("auth-api", decodedJWT.getIssuer());
        assertEquals("testUser", decodedJWT.getSubject());
    }

    @Test
    public void testValidateToken_JWTVerificationException() {
        // Given
        String token = "invalid-token";

        // When
        DecodedJWT decodedJWT = underTest.validateToken(token);

        // Then
        assertNull(decodedJWT);
    }

    @Test
    public void testGetExpiration() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // Given
        Method getExpirationMethod = TokenServiceImpl.class.getDeclaredMethod("getExpiration");
        getExpirationMethod.setAccessible(true);

        // When
        Instant expiration = (Instant) getExpirationMethod.invoke(underTest);

        // Then
        Instant expectedExpiration = LocalDateTime.now()
                .plusHours(1)
                .toInstant(ZoneOffset.of("-03:00"));
        assertTrue(expiration.isAfter(Instant.now()));
        assertTrue(expiration.isBefore(expectedExpiration.plusSeconds(1)));
    }
}