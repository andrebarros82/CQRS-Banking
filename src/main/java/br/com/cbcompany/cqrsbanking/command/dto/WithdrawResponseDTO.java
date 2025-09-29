/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.dto;

import br.com.cbcompany.cqrsbanking.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) utilizado para representar a resposta de uma
 * operação de saque.
 * <p>
 * Contém informações sobre o resultado da transação, incluindo o novo saldo do
 * usuário, o tipo de transação realizada e o timestamp em que a operação
 * ocorreu.
 * </p>
 *
 * <p>
 * Exemplo de resposta JSON:</p>
 * <pre>
 * {
 *   "message": "Saque realizado com sucesso.",
 *   "newBalance": 800.00,
 *   "timestamp": "2025-09-28T15:30:45",
 *   "transactionType": "SAQUE"
 * }
 * </pre>
 *
 * <p>
 * Este DTO é usado no endpoint {@code POST /api/commands/users/withdraw}.</p>
 *
 * @author andre
 */
@Schema(description = "Resposta da operação de saque, contendo detalhes da transação e saldo atualizado.")
public record WithdrawResponseDTO(
        /**
         * Mensagem indicando o resultado da operação.
         */
        @Schema(description = "Mensagem informando o resultado da operação de saque.",
                example = "Saque realizado com sucesso.")
        String message,
        /**
         * Novo saldo do usuário após a realização do saque.
         */
        @Schema(description = "Novo saldo do usuário após a transação.", example = "800.00")
        BigDecimal newBalance,
        /**
         * Data e hora em que a transação foi realizada.
         */
        @Schema(description = "Timestamp da transação.", example = "2025-09-28T15:30:45")
        LocalDateTime timestamp,
        /**
         * Tipo da transação realizada (ex.: DEPÓSITO, SAQUE, PAGAMENTO).
         */
        @Schema(description = "Tipo da transação realizada.", example = "SAQUE")
        String transactionType
        ) {

    /**
     * Construtor auxiliar que já define a mensagem padrão e o tipo de transação
     * como SAQUE.
     *
     * @param newBalance Novo saldo do usuário após o saque
     * @param timestamp Data e hora em que a transação ocorreu
     */
    public WithdrawResponseDTO(BigDecimal newBalance, LocalDateTime timestamp) {
        this("Saque realizado com sucesso.", newBalance, timestamp, TransactionType.SAQUE.name());
    }
}
