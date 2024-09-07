package com.e3gsix.fiap.tech_challenge_5_credentials.security;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.exception.StandardError;
import com.e3gsix.fiap.tech_challenge_5_credentials.repository.UserRepository;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.AuthenticationControllerImpl.*;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS_FIND_BY_ID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    private final Map<String, HttpMethod> PERMITTED_RESOURCE = Map.of(
            URL_AUTH.concat(URl_AUTH_REGISTER), HttpMethod.POST,
            URL_AUTH.concat(URl_AUTH_LOGIN), HttpMethod.POST,
            URL_USERS.concat(URL_USERS_FIND_BY_ID), HttpMethod.GET
    );

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (isPermittedEndpoint(request.getRequestURI(), request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.recoverToken(request);

        if (Objects.isNull(token)) {
            unauthorizeResponse(request, response, "Token não foi encontrado.");
            return;
        }

        String username = tokenService.validateToken(token);
        UserDetails user = userRepository.findByUsername(username);

        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void unauthorizeResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            String message
    ) throws IOException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = StandardError.create(status, message, request.getRequestURI());

        String jsonResponse = createObjectMapper().writeValueAsString(error);

        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setContentType("application/json");
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }

    private boolean isPermittedEndpoint(String endpoint, String method) {
        if (!PERMITTED_RESOURCE.keySet().contains(endpoint)) return false;

        HttpMethod resourceMethod = PERMITTED_RESOURCE.get(endpoint);

        return resourceMethod.name().equals(method);
    }

    private String recoverToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) return null;
        return authorization.replace("Bearer ", "");
    }
}
