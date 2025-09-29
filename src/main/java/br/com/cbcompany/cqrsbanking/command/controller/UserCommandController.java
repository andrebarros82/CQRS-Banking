/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.controller;

import br.com.cbcompany.cqrsbanking.command.dto.RegisterUserRequestDTO;
import br.com.cbcompany.cqrsbanking.command.service.UserCommandService;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pelos endpoints de comandos relacionados ao gerenciamento de usuários,
 * como o registro de novos usuários.
 *
 * <p>Seguindo o padrão CQRS, este controlador é focado apenas em operações de escrita
 * (commands), enquanto as operações de leitura (queries) devem ser implementadas
 * em controladores específicos para consultas.</p>
 *
 * <p>Todos os endpoints expostos aqui são destinados a comandos que alteram o estado do sistema.</p>
 * 
 * @author andre
 */
@RestController
@RequestMapping("/api/commands/users")
@Tag(name = "User Commands", description = "Operações de comando relacionadas ao gerenciamento de usuários")
public class UserCommandController {

    /**
     * Serviço responsável por lidar com os comandos referentes ao gerenciamento de usuários.
     */
    private final UserCommandService userCommandService;

    /**
     * Construtor para injeção de dependências.
     *
     * @param userCommandService serviço de comando de usuários
     */
    public UserCommandController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Endpoint responsável por registrar um novo usuário no sistema.
     *
     * @param cmd objeto {@link RegisterUserRequestDTO} contendo os dados necessários para o registro.
     * @return resposta HTTP contendo o usuário recém-criado.
     */
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema com as informações fornecidas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuário registrado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserModel.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos fornecidos na requisição"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno do servidor"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para registrar um novo usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterUserRequestDTO.class))
            )
            @Valid
            @org.springframework.web.bind.annotation.RequestBody RegisterUserRequestDTO cmd) {

        UserModel created = userCommandService.registerUser(cmd);
        return ResponseEntity.ok(created);
    }
}