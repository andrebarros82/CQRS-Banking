/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.controller;

import br.com.cbcompany.cqrsbanking.command.service.DepositCommandService;
import br.com.cbcompany.cqrsbanking.dto.DepositRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/commands/users")
public class DepositCommandController {

    private final DepositCommandService depositCommandService;

    public DepositCommandController(DepositCommandService depositCommandService) {
        this.depositCommandService = depositCommandService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @Valid @RequestBody DepositRequest request, Authentication authentication) {

        String login = authentication.getName();
        BigDecimal novoSaldo = depositCommandService.deposit(login, request);
        return ResponseEntity.ok("Depósito realizado com sucesso. Novo saldo: " + novoSaldo);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @Valid @RequestBody DepositRequest request, Authentication authentication) {

        String login = authentication.getName();
        BigDecimal novoSaldo = depositCommandService.withdraw(login, request);
        return ResponseEntity.ok("Saque realizado com sucesso. Novo saldo: " + novoSaldo);
    }
    
    @PostMapping("/payBill")
    public ResponseEntity<String> payBill(
            @Valid @RequestBody DepositRequest request, Authentication authentication) {

        String login = authentication.getName();
        BigDecimal novoSaldo = depositCommandService.payBill(login, request);
        return ResponseEntity.ok("Pagamento realizado com sucesso. Novo saldo: " + novoSaldo);
    }
}
