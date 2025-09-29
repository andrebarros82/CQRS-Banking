/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * DTO que representa a resposta após o cadastro de um novo usuário.
 *
 * <p>
 * Este objeto é retornado pelos endpoints de registro de usuário e contém
 * informações essenciais sobre o usuário recém-criado, sem expor a senha.</p>
 *
 * <p>
 * Utilizado para documentar de forma clara a resposta da API no
 * Swagger/OpenAPI.</p>
 *
 * @author andre
 */
@Schema(description = "Resposta retornada após o cadastro de um usuário")
public record RegisterUserResponseDTO(
        /**
         * Identificador único do usuário no sistema.
         */
        @Schema(description = "ID único do usuário", example = "1")
        Long id,
        /**
         * Nome completo do usuário.
         */
        @Schema(description = "Nome completo do usuário", example = "André Barros")
        String nomeCompleto,
        /**
         * CPF do usuário.
         */
        @Schema(description = "CPF do usuário", example = "123.456.789-00")
        String cpf,
        /**
         * Login do usuário.
         */
        @Schema(description = "Login utilizado pelo usuário", example = "andrebarros")
        String login,
        /**
         * Saldo monetário atual da conta do usuário.
         */
        @Schema(description = "Saldo atual da conta do usuário", example = "1500.00")
        BigDecimal saldoMonetarioAtual
        ) {

}
