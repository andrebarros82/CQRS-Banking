/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.service;

import br.com.cbcompany.cqrsbanking.model.Transaction;
import br.com.cbcompany.cqrsbanking.model.TransactionType;
import br.com.cbcompany.cqrsbanking.model.User;
import br.com.cbcompany.cqrsbanking.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void registrarTransacao(User user, TransactionType tipo, BigDecimal valor) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTipo(tipo);
        transaction.setValor(valor);
        transaction.setDataHora(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}
