/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.controller;

import br.com.cbcompany.cqrsbanking.query.dto.UserViewResponseDTO;
import br.com.cbcompany.cqrsbanking.query.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para consultas de informações de usuários.
 *
 * <p>
 * Esta classe expõe endpoints de leitura relacionados aos usuários, mantendo a
 * separação de responsabilidades do padrão CQRS.</p>
 *
 * <p>
 * Os endpoints podem ser utilizados para recuperar informações de usuários por
 * ID.</p>
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/queries/users")
@Tag(name = "User", description = "Operações de consulta de informações de usuários")
public class UserQueryController {

    private final UserQueryService userQueryService;

    /**
     * Construtor para injeção do serviço de consulta de usuários.
     *
     * @param userQueryService serviço responsável por recuperar informações de
     * usuários
     */
    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * Endpoint para recuperar informações de um usuário pelo seu ID.
     *
     * @param id identificador do usuário
     * @return {@link UserViewResponseDTO} com os dados do usuário
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Consulta usuário por ID",
            description = "Recupera os dados de um usuário específico pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso"),
        @ApiResponse(
                responseCode = "401",
                description = "Usuário não autenticado",
                content = @Content(schema = @Schema(implementation = ErrorUserResponseDTO.class))
        ),})
    public ResponseEntity<UserViewResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userQueryService.getUserById(id));
    }
}
