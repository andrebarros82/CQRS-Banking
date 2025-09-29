/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.controller;

import br.com.cbcompany.cqrsbanking.cache.dto.HistoricoResponseDTO;
import br.com.cbcompany.cqrsbanking.query.service.HistoricoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para consultas do histórico de transações do usuário.
 *
 * <p>
 * Esta classe expõe endpoints para recuperar o histórico de transações,
 * utilizando o padrão CQRS para separar comandos (writes) de consultas
 * (reads).</p>
 *
 * <p>
 * Todos os endpoints requerem que o usuário esteja autenticado via JWT.</p>
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/queries/users")
@Tag(name = "Historico", description = "Operações de consulta do histórico de transações do usuário")
public class HistoricoQueryController {

    private final HistoricoQueryService historicoQueryService;

    /**
     * Construtor para injeção do serviço de consulta de histórico.
     *
     * @param historicoQueryService serviço que recupera o histórico de
     * transações
     */
    public HistoricoQueryController(HistoricoQueryService historicoQueryService) {
        this.historicoQueryService = historicoQueryService;
    }

    /**
     * Endpoint para recuperar o histórico de transações do usuário autenticado.
     *
     * <p>
     * O retorno inclui o saldo total atual e a lista das transações mais
     * recentes.</p>
     *
     * @param authentication objeto que representa o usuário autenticado
     * @return {@link HistoricoResponseDTO} com saldo total e lista de
     * transações
     */
    @GetMapping("/transactions")
    @Operation(
            summary = "Consulta histórico de transações",
            description = "Recupera o saldo total atual e as transações recentes do usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
        @ApiResponse(
                responseCode = "400",
                description = "Saldo insuficiente ou dados inválidos",
                content = @Content(schema = @Schema(implementation = ErrorUserResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Usuário não autenticado",
                content = @Content(schema = @Schema(implementation = ErrorUserResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Usuário não encontrado",
                content = @Content(schema = @Schema(implementation = ErrorUserResponseDTO.class))
        )
    })
    public ResponseEntity<HistoricoResponseDTO> getHistorico(Authentication authentication) {
        String login = authentication.getName();
        HistoricoResponseDTO response = historicoQueryService.getHistorico(login);
        return ResponseEntity.ok(response);
    }
}
