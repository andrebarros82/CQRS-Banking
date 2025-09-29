/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa uma transação armazenada no cache
 * Redis.
 *
 * <p>
 * Esta classe é utilizada para manter um registro eficiente das transações
 * recentes do usuário, permitindo consultas rápidas sem acessar o banco de
 * dados principal.</p>
 *
 * <p>
 * Campos:</p>
 * <ul>
 * <li>{@code tipo} - Tipo da transação (DEPÓSITO, SAQUE, PAGAMENTO).</li>
 * <li>{@code valor} - Valor monetário da transação.</li>
 * <li>{@code dataHora} - Data e hora em que a transação ocorreu.</li>
 * </ul>
 *
 * @author andre
 * @param tipo tipo da transação
 * @param valor valor da transação
 * @param dataHora data e hora da transação
 */
public record TransactionCacheDTO(
        String tipo,
        BigDecimal valor,
        LocalDateTime dataHora
        ) {

}
