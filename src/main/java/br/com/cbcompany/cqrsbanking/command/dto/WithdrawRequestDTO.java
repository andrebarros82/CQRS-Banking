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
 * DTO (Data Transfer Object) utilizado para requisições de saque de valores da
 * conta do usuário.
 * <p>
 * Esta classe é utilizada pelos endpoints REST para receber as informações
 * necessárias para processar a operação de saque.
 * </p>
 *
 * <p>
 * <b>Validações aplicadas:</b></p>
 * <ul>
 * <li>{@code @NotNull} – garante que o valor do saque seja informado.</li>
 * <li>{@code @Min(1)} – garante que o valor do saque seja maior ou igual a
 * 1.</li>
 * </ul>
 *
 * <p>
 * Exemplo de requisição JSON:</p>
 * <pre>
 * {
 *   "amount": 150.00
 * }
 * </pre>
 *
 * @author andre
 */
@Schema(description = "Objeto de requisição para realizar um saque na conta do usuário.")
public record WithdrawRequestDTO(
        /**
         * Valor monetário a ser sacado da conta do usuário.
         * <p>
         * O valor deve ser informado em formato decimal, não pode ser nulo e
         * deve ser no mínimo 1.
         * </p>
         */
        @Schema(
                description = "Valor do saque a ser realizado.",
                example = "150.00",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor do saque é obrigatório.")
        @Min(value = 1, message = "O valor mínimo para saque é 1.")
        BigDecimal amount
        ) {

}
