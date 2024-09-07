package com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception;

public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
