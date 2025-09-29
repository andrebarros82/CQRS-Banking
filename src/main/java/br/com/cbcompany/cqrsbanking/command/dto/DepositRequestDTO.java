/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) que representa a requisição de depósito.
 * <p>
 * Esta classe é utilizada nos endpoints REST para receber as informações
 * necessárias para realizar um depósito na conta de um usuário.
 * </p>
 *
 * <p>
 * <b>Validações aplicadas:</b></p>
 * <ul>
 * <li>{@code @NotNull} – garante que o valor do depósito não seja nulo.</li>
 * <li>{@code @Min(1)} – garante que o valor mínimo do depósito seja 1.</li>
 * </ul>
 *
 * <p>
 * Exemplo JSON enviado para o endpoint:</p>
 * <pre>
 * {
 *   "amount": 150.00
 * }
 * </pre>
 *
 * @author andre
 */
@Schema(description = "Objeto de requisição para realizar um depósito em uma conta de usuário.")
public record DepositRequestDTO(
        /**
         * Valor monetário a ser depositado na conta do usuário.
         * <p>
         * Este valor deve ser informado em formato decimal e não pode ser nulo.
         * O valor mínimo aceito é 1.
         * </p>
         */
        @Schema(
                description = "Valor a ser depositado na conta do usuário.",
                example = "150.00",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor do depósito é obrigatório.")
        @Min(value = 1, message = "O valor mínimo para depósito é 1.")
        BigDecimal amount
        ) {

}
