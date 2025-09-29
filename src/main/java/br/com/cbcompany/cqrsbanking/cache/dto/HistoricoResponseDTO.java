/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.dto;

import java.util.List;

/**
 * DTO (Data Transfer Object) que representa o histórico completo de transações
 * de um usuário, incluindo o saldo total atual e a lista de transações
 * realizadas.
 *
 * <p>
 * Essa classe é utilizada para retornar informações de consulta (Query) do
 * cache Redis ou do banco de dados, seguindo o padrão CQRS.</p>
 *
 * @author andre
 * @param SaldoTotal valor total atual da conta do usuário, representado como
 * string para compatibilidade
 * @param Historico lista de transações realizadas pelo usuário, contendo tipo,
 * valor e data/hora
 */
public record HistoricoResponseDTO(
        String SaldoTotal,
        List<HistoricoDTO> Historico
        ) {

}
