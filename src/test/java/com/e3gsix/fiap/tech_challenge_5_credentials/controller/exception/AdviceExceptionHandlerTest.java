package com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdviceExceptionHandlerTest {

    private AdviceExceptionHandler adviceExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        adviceExceptionHandler = new AdviceExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest_WhenIsIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<StandardError> response = adviceExceptionHandler.handleIllegalArgumentException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody().message());
        assertEquals("/test-uri", response.getBody().path());
    }

    @Test
    void handleUnsupportedOperationException_ShouldReturnBadRequest_WhenIsUnsupportedOperationException() {
        UnsupportedOperationException ex = new UnsupportedOperationException("Operation not supported");

        ResponseEntity<StandardError> response = adviceExceptionHandler.handleUnsupportedOperationException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Operation not supported", response.getBody().message());
        assertEquals("/test-uri", response.getBody().path());
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound_WhenIsNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");

        ResponseEntity<StandardError> response = adviceExceptionHandler.handleNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().message());
        assertEquals("/test-uri", response.getBody().path());
    }

    @Test
    void handleResourceAlreadyExistException_ShouldReturnConflict_WhenIsResourceAlreadyExistException() {
        ResourceAlreadyExistException ex = new ResourceAlreadyExistException("Resource already exists");

        ResponseEntity<StandardError> response = adviceExceptionHandler.handleResourceAlreadyExistException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Resource already exists", response.getBody().message());
        assertEquals("/test-uri", response.getBody().path());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError_WhenIsException() {
        Exception ex = new Exception("Internal server error");

        ResponseEntity<StandardError> response = adviceExceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().message());
        assertEquals("/test-uri", response.getBody().path());
    }
}