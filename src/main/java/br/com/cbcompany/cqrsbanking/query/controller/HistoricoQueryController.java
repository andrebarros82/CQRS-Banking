/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.controller;

import br.com.cbcompany.cqrsbanking.cache.dto.HistoricoResponseDTO;
import br.com.cbcompany.cqrsbanking.query.service.HistoricoQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/queries/users")
public class HistoricoQueryController {

    private final HistoricoQueryService historicoQueryService;

    public HistoricoQueryController(HistoricoQueryService historicoQueryService) {
        this.historicoQueryService = historicoQueryService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<HistoricoResponseDTO> getHistorico(Authentication authentication) {
        String login = authentication.getName();
        HistoricoResponseDTO response = historicoQueryService.getHistorico(login);
        return ResponseEntity.ok(response);
    }
}
