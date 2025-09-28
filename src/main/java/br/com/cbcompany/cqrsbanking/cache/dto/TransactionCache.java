/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author andre
 */
public record TransactionCache(
        String tipo,
        BigDecimal valor,
        LocalDateTime dataHora
        ) {

}
