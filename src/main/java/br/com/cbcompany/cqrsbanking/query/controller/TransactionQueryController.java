/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.controller;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCache;
import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import java.util.List;
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
public class TransactionQueryController {

//    private final TransactionCacheService transactionCacheService;
//
//    public TransactionQueryController(TransactionCacheService transactionCacheService) {
//        this.transactionCacheService = transactionCacheService;
//    }
//
//    @GetMapping("/transactions")
//    public ResponseEntity<List<TransactionCache>> getTransactions(Authentication authentication) {
//        String login = authentication.getName();
//        List<TransactionCache> transactions = transactionCacheService.getTransactionsFromCache(login);
//        return ResponseEntity.ok(transactions);
//    }
}
