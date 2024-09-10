package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.AuthenticationController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserCreateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserLoginRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserLoginResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.AuthenticationService;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.AuthenticationControllerImpl.URL_AUTH;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;
import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS_FIND_BY_ID;

@Tag(name = "Autenticação", description = "Gestão de credenciais de acesso.")
@RestController
@RequestMapping(URL_AUTH)
public class AuthenticationControllerImpl implements AuthenticationController {

    public static final String URL_AUTH = "/auth";
    public static final String URl_AUTH_REGISTER = "/register";
    public static final String URl_AUTH_LOGIN = "/login";

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationControllerImpl(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Cadastro de usuário",
            description = "Recurso responsável por registrar usuários na base.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário registrado com sucesso.",
                            headers = {
                                    @Header(
                                            name = "Location",
                                            description = "Localização do recurso criado."
                                    )
                            }),
                    @ApiResponse(responseCode = "409", description = "Username já está em uso.")
            }
    )
    @Override
    @PostMapping(URl_AUTH_REGISTER)
    public ResponseEntity register(
            @RequestBody UserCreateRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UUID registeredId = this.userService.register(request);

        URI uri = uriComponentsBuilder.path(URL_USERS.concat(URL_USERS_FIND_BY_ID))
                .buildAndExpand(registeredId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Gerar token",
            description = "Endpoint que gera um token de acordo com as credenciais de acesso de um usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Credenciais válidas."),
                    @ApiResponse(responseCode = "404", description = "Usuário com ID informado não foi encontrado.")
            }
    )
    @Override
    @PostMapping(URl_AUTH_LOGIN)
    public ResponseEntity login(@RequestBody UserLoginRequest request) {
        String token = this.authenticationService.login(request);

        return ResponseEntity.ok(new UserLoginResponse(token));
    }
}
