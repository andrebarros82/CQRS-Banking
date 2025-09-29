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
 * DTO (Data Transfer Object) utilizado para requisições de pagamento de contas.
 * <p>
 * Esta classe é usada pelos endpoints REST para receber as informações
 * necessárias para processar o pagamento de uma conta.
 * </p>
 *
 * <p>
 * <b>Validações aplicadas:</b></p>
 * <ul>
 * <li>{@code @NotNull} – garante que o valor do pagamento não seja nulo.</li>
 * <li>{@code @Min(1)} – garante que o valor mínimo do pagamento seja 1.</li>
 * </ul>
 *
 * <p>
 * Exemplo de requisição JSON:</p>
 * <pre>
 * {
 *   "amount": 250.00
 * }
 * </pre>
 *
 * @author andre
 */
@Schema(description = "Objeto de requisição para realizar o pagamento de uma conta.")
public record PayBillRequestDTO(
        /**
         * Valor monetário a ser pago na conta.
         * <p>
         * O valor deve ser informado em formato decimal, não pode ser nulo e
         * deve ser no mínimo 1.
         * </p>
         */
        @Schema(
                description = "Valor do pagamento da conta.",
                example = "250.00",
                minimum = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor do pagamento é obrigatório.")
        @Min(value = 1, message = "O valor mínimo para pagamento é 1.")
        BigDecimal amount
        ) {

}
