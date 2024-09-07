package com.e3gsix.fiap.tech_challenge_5_credentials.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtils {

    public static String encrypt(String value) {
        return new BCryptPasswordEncoder().encode(value);
    }
}
