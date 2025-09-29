/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.dto;

import java.math.BigDecimal;

/**
 * DTO de resposta para a visualização de informações de um usuário.
 *
 * <p>
 * Esta classe é utilizada para retornar dados de usuários em endpoints de
 * consulta (queries) no padrão CQRS. Todos os campos são somente leitura,
 * garantindo imutabilidade.</p>
 *
 * <p>
 * Os atributos incluem dados de identificação e saldo monetário atual do
 * usuário.</p>
 *
 * @author andre
 * @param id Identificador único do usuário
 * @param nomeCompleto Nome completo do usuário
 * @param cpf CPF do usuário
 * @param login Login do usuário
 * @param saldoMonetarioAtual Saldo monetário atual da conta do usuário
 */
public record UserViewResponseDTO(
        Long id,
        String nomeCompleto,
        String cpf,
        String login,
        BigDecimal saldoMonetarioAtual
        ) {

}
