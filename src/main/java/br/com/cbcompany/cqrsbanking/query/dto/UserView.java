/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.dto;

import java.math.BigDecimal;

/**
 *
 * @author andre
 */
public record UserView(
        Long id,
        String nomeCompleto,
        String cpf,
        String login,
        BigDecimal saldoMonetarioAtual
        ) {

}
