/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.dto;

import java.util.List;

/**
 *
 * @author andre
 */
public record HistoricoResponseDTO(
        String SaldoTotal,
        List<Historico> Historico
        ) {

}
