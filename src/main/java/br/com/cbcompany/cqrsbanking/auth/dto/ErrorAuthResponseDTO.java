/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO que representa a resposta de erro padronizada para os endpoints da API.
 * 
 * <p>Este objeto é utilizado para retornar informações sobre falhas de validação,
 * autenticação ou erros internos do servidor, permitindo que o consumidor da API
 * compreenda o motivo do erro.</p>
 * 
 * @author andre
 */
@Schema(description = "Resposta padrão de erro para falhas na API")
public record ErrorAuthResponseDTO(
        
        /**
         * Mensagem descritiva do erro.
         */
        @Schema(description = "Mensagem descritiva do erro", example = "Usuário não encontrado")
        String message,
        
        /**
         * Código HTTP do erro.
         */
        @Schema(description = "Código HTTP associado ao erro", example = "400")
        int status,
        
        /**
         * Data e hora em que o erro ocorreu.
         */
        @Schema(description = "Timestamp da ocorrência do erro", example = "2025-09-28T12:34:56")
        String timestamp
) {}