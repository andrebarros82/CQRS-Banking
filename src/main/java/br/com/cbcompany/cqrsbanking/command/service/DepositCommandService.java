/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.service;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCache;
import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import br.com.cbcompany.cqrsbanking.dto.DepositRequest;
import br.com.cbcompany.cqrsbanking.exception.UserNotFoundException;
import br.com.cbcompany.cqrsbanking.model.TransactionType;
import br.com.cbcompany.cqrsbanking.model.User;
import br.com.cbcompany.cqrsbanking.service.TransactionService;
import br.com.cbcompany.cqrsbanking.service.UserService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author andre
 */
@Service
public class DepositCommandService {

    private final UserService userService;
    private final TransactionService transactionService;
    private final TransactionCacheService transactionCacheService;

    public DepositCommandService(UserService userService, TransactionService transactionService, TransactionCacheService transactionCacheService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionCacheService = transactionCacheService;
    }

    @Transactional
    public BigDecimal deposit(String login, DepositRequest request) {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal saldoAtual = user.getSaldoMonetarioAtual();
        BigDecimal deposito = request.amount();
        BigDecimal novoSaldo;

        if (saldoAtual.compareTo(BigDecimal.ZERO) < 0) {
            // Usuário está negativado
            BigDecimal divida = saldoAtual.abs();  // valor absoluto da dívida
            BigDecimal juros = divida.multiply(new BigDecimal("0.0102"))
                    .setScale(2, RoundingMode.HALF_UP); // juros com 2 casas decimais
            BigDecimal totalDivida = divida.add(juros);

            // Quanto do depósito será usado para quitar a dívida
            BigDecimal aplicado = deposito.min(totalDivida);

            // Novo saldo após pagar dívida + juros
            novoSaldo = saldoAtual.add(aplicado).subtract(juros);

            // Se sobrar valor do depósito, aumenta saldo positivo
            BigDecimal restante = deposito.subtract(aplicado);
            if (restante.compareTo(BigDecimal.ZERO) > 0) {
                novoSaldo = novoSaldo.add(restante);
            }
        } else {
            // Saldo positivo: apenas adiciona o depósito
            novoSaldo = saldoAtual.add(deposito);
        }

        // Ajusta o saldo final para 2 casas decimais
        novoSaldo = novoSaldo.setScale(2, RoundingMode.HALF_UP);
        user.setSaldoMonetarioAtual(novoSaldo);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.DEPOSITO, deposito);

        transactionCacheService.addTransactionToCache(
                login,
                new TransactionCache(
                        TransactionType.DEPOSITO.name(),
                        deposito,
                        java.time.LocalDateTime.now()
                )
        );

        return novoSaldo;
    }

    @Transactional
    public BigDecimal withdraw(String login, DepositRequest request) {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal withdrawAmount = request.amount();
        BigDecimal newBalance = user.getSaldoMonetarioAtual().subtract(withdrawAmount);

        // Permite saldo negativo
        user.setSaldoMonetarioAtual(newBalance);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.SAQUE, withdrawAmount);

        transactionCacheService.addTransactionToCache(
                login,
                new TransactionCache(TransactionType.SAQUE.name(), withdrawAmount, java.time.LocalDateTime.now())
        );

        return newBalance;
    }

    @Transactional
    public BigDecimal payBill(String login, DepositRequest request) {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal payAmount = request.amount();
        BigDecimal newBalance = user.getSaldoMonetarioAtual().subtract(payAmount);

        // Permite saldo negativo ao pagar contas
        user.setSaldoMonetarioAtual(newBalance);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.PAGAMENTO, payAmount);

        transactionCacheService.addTransactionToCache(
                login,
                new TransactionCache(TransactionType.PAGAMENTO.name(), payAmount, java.time.LocalDateTime.now())
        );

        return newBalance;
    }
}
