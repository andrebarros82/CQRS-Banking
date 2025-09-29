/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.dto;

/**
 * DTO (Data Transfer Object) que representa uma única transação realizada pelo usuário.
 *
 * <p>Utilizado como elemento da lista {@link HistoricoResponseDTO#Historico} para retornar
 * o histórico completo de transações.</p>
 *
 * @author andre
 * @param tipo tipo da transação (DEPÓSITO, SAQUE, PAGAMENTO)
 * @param valor valor monetário da transação, representado como string
 * @param data data e hora em que a transação ocorreu, no formato "dd-MM-yyyy HH:mm:ss"
 */
public record HistoricoDTO(
        String tipo,
        String valor,
        String data
) {

}
