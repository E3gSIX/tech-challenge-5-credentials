package com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl;

import com.e3gsix.fiap.tech_challenge_5_credentials.controller.UserController;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.request.UserUpdateRequest;
import com.e3gsix.fiap.tech_challenge_5_credentials.model.dto.response.UserResponse;
import com.e3gsix.fiap.tech_challenge_5_credentials.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.e3gsix.fiap.tech_challenge_5_credentials.controller.impl.UserControllerImpl.URL_USERS;

@Tag(name = "Usuários", description = "Gerenciamento de usuários.")
@RestController
@RequestMapping(URL_USERS)
public class UserControllerImpl implements UserController {

    public static final String URL_USERS = "/users";
    public static final String URL_USERS_FIND_BY_ID = "/{id}";
    public static final String URL_UPDATE = "/{id}";

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Buscar usuário pelo ID",
            description = "Este recurso realiza a busca de um usuário na base de dados através do ID informado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário com ID informado não foi encontrado.")
            }
    )
    @Override
    @GetMapping(URL_USERS_FIND_BY_ID)
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse userFound = this.userService.findById(id);

        return ResponseEntity.ok(userFound);
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "O endpoint permite que ADMINISTRADORES atualizem informações pertinentes aos usuários.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
                    @ApiResponse(responseCode = "401", description = "Token não foi informado ou está inválido."),
                    @ApiResponse(responseCode = "403", description = "Requisitante não tem permissões de administrador."),
                    @ApiResponse(responseCode = "404", description = "Usuário com ID informado não foi encontrado.")
            }
    )
    @Override
    @PutMapping(URL_UPDATE)
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        UserResponse updatedUser = this.userService.update(id, request);

        return ResponseEntity.ok(updatedUser);
    }
}
