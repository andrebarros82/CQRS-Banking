/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.service;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCacheDTO;
import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import br.com.cbcompany.cqrsbanking.command.dto.DepositRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.DepositResponseDTO;
import br.com.cbcompany.cqrsbanking.command.dto.PayBillRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.PayBillResponseDTO;
import br.com.cbcompany.cqrsbanking.command.dto.WithdrawRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.WithdrawResponseDTO;
import br.com.cbcompany.cqrsbanking.exception.UserNotFoundException;
import br.com.cbcompany.cqrsbanking.model.TransactionType;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.service.TransactionService;
import br.com.cbcompany.cqrsbanking.service.UserService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável por executar os comandos relacionados às transações de usuários
 * (DEPÓSITO, SAQUE e PAGAMENTO), garantindo persistência no banco de dados e atualização
 * do cache Redis para consultas rápidas.
 * 
 * <p>Seguindo o padrão CQRS, esta classe concentra apenas operações de escrita (comandos),
 * enquanto a leitura das transações deve ser feita através de um serviço de consulta.</p>
 * 
 * <p>Inclui regras de negócio como cálculo de juros em saldo negativo, atualização do saldo
 * do usuário e registro das transações no cache para histórico.</p>
 * 
 * @author andre
 */
@Service
public class UserFinancialCommandService {

    /** Serviço responsável pela manipulação de dados do usuário */
    private final UserService userService;

    /** Serviço responsável por registrar transações no banco de dados */
    private final TransactionService transactionService;

    /** Serviço responsável por manter histórico de transações em cache Redis */
    private final TransactionCacheService transactionCacheService;

    /**
     * Construtor que injeta os serviços necessários para operações de transação.
     *
     * @param userService Serviço de manipulação de usuários
     * @param transactionService Serviço de persistência de transações
     * @param transactionCacheService Serviço de cache das transações
     */
    public UserFinancialCommandService(UserService userService, TransactionService transactionService, TransactionCacheService transactionCacheService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.transactionCacheService = transactionCacheService;
    }

    /**
     * Realiza um depósito na conta do usuário.
     * <p>Se o usuário estiver negativado, o depósito é usado primeiro para quitar parte da dívida
     * acrescida de juros. O saldo restante é adicionado ao saldo positivo.</p>
     *
     * @param login Login do usuário autenticado
     * @param request Dados do depósito (valor)
     * @return DepositResponseDTO contendo novo saldo, timestamp e tipo de transação
     * @throws UserNotFoundException se o usuário não existir
     */
    @Transactional
    public DepositResponseDTO deposit(String login, DepositRequestDTO request) {
        UserModel user = userService.findByLogin(login).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal currentBalance = user.getSaldoMonetarioAtual();
        BigDecimal deposit = request.amount();
        BigDecimal newBalance;

        if (currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            // Usuário está negativado
            BigDecimal divida = currentBalance.abs();  
            BigDecimal juros = divida.multiply(new BigDecimal("0.0102")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalDivida = divida.add(juros);

            BigDecimal aplicado = deposit.min(totalDivida);
            newBalance = currentBalance.add(aplicado).subtract(juros);

            BigDecimal restante = deposit.subtract(aplicado);
            if (restante.compareTo(BigDecimal.ZERO) > 0) {
                newBalance = newBalance.add(restante);
            }
        } else {
            // Saldo positivo: adiciona o depósito
            newBalance = currentBalance.add(deposit);
        }

        newBalance = newBalance.setScale(2, RoundingMode.HALF_UP);
        user.setSaldoMonetarioAtual(newBalance);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.DEPOSITO, deposit);

        // Registra transação no cache Redis
        TransactionCacheDTO transactionCacheDTO = new TransactionCacheDTO(
                TransactionType.DEPOSITO.name(), deposit, java.time.LocalDateTime.now());
        transactionCacheService.addTransactionToCache(login, transactionCacheDTO);

        return new DepositResponseDTO(newBalance, transactionCacheDTO.dataHora());
    }

    /**
     * Realiza um saque na conta do usuário.
     * <p>Permite saldo negativo.</p>
     *
     * @param login Login do usuário autenticado
     * @param request Dados do saque (valor)
     * @return WithdrawResponseDTO com novo saldo, timestamp e tipo de transação
     * @throws UserNotFoundException se o usuário não existir
     */
    @Transactional
    public WithdrawResponseDTO withdraw(String login, WithdrawRequestDTO request) {
        UserModel user = userService.findByLogin(login).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal withdrawAmount = request.amount();
        BigDecimal newBalance = user.getSaldoMonetarioAtual().subtract(withdrawAmount);

        user.setSaldoMonetarioAtual(newBalance);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.SAQUE, withdrawAmount);

        TransactionCacheDTO transactionCacheDTO = new TransactionCacheDTO(
                TransactionType.SAQUE.name(), withdrawAmount, java.time.LocalDateTime.now());
        transactionCacheService.addTransactionToCache(login, transactionCacheDTO);

        return new WithdrawResponseDTO(newBalance, transactionCacheDTO.dataHora());
    }

    /**
     * Realiza o pagamento de uma conta utilizando o saldo do usuário.
     * <p>Permite saldo negativo.</p>
     *
     * @param login Login do usuário autenticado
     * @param request Dados do pagamento (valor)
     * @return PayBillResponseDTO com novo saldo, timestamp e tipo de transação
     * @throws UserNotFoundException se o usuário não existir
     */
    @Transactional
    public PayBillResponseDTO payBill(String login, PayBillRequestDTO request) {
        UserModel user = userService.findByLogin(login).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + login));

        BigDecimal payAmount = request.amount();
        BigDecimal newBalance = user.getSaldoMonetarioAtual().subtract(payAmount);

        user.setSaldoMonetarioAtual(newBalance);
        userService.save(user);

        transactionService.registrarTransacao(user, TransactionType.PAGAMENTO, payAmount);

        TransactionCacheDTO transactionCacheDTO = new TransactionCacheDTO(
                TransactionType.PAGAMENTO.name(), payAmount, java.time.LocalDateTime.now());
        transactionCacheService.addTransactionToCache(login, transactionCacheDTO);

        return new PayBillResponseDTO(newBalance, transactionCacheDTO.dataHora());
    }
}