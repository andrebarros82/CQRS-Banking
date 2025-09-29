/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking;

import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import br.com.cbcompany.cqrsbanking.command.dto.DepositRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.WithdrawRequestDTO;
import br.com.cbcompany.cqrsbanking.command.service.DepositCommandService;
import br.com.cbcompany.cqrsbanking.exception.UserNotFoundException;
import br.com.cbcompany.cqrsbanking.model.TransactionType;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.service.TransactionService;
import br.com.cbcompany.cqrsbanking.service.UserService;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author andre
 */
class DepositCommandServiceTest {

    private UserService userService;
    private TransactionService transactionService;
    private TransactionCacheService transactionCacheService;
    private DepositCommandService depositCommandService;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        transactionService = mock(TransactionService.class);
        transactionCacheService = mock(TransactionCacheService.class);
        depositCommandService = new DepositCommandService(userService, transactionService, transactionCacheService);
    }

    @Test
    void testDeposit_PositiveBalance() {
        UserModel user = new UserModel();
        user.setSaldoMonetarioAtual(BigDecimal.valueOf(100));

        when(userService.findByLogin("john")).thenReturn(Optional.of(user));

        DepositRequestDTO request = new DepositRequestDTO(BigDecimal.valueOf(50));
        var response = depositCommandService.deposit("john", request);

        assertEquals(BigDecimal.valueOf(150).setScale(2), user.getSaldoMonetarioAtual());
        assertEquals("Depósito realizado com sucesso.", response.message());

        // Verifica se transação foi registrada
        verify(transactionService).registrarTransacao(user, TransactionType.DEPOSITO, BigDecimal.valueOf(50));
        verify(transactionCacheService).addTransactionToCache(eq("john"), any());
    }

    @Test
    void testDeposit_UserNotFound() {
        when(userService.findByLogin("john")).thenReturn(Optional.empty());

        DepositRequestDTO request = new DepositRequestDTO(BigDecimal.valueOf(50));

        assertThrows(UserNotFoundException.class, () -> depositCommandService.deposit("john", request));
    }

    @Test
    void testWithdraw_PositiveBalance() {
        UserModel user = new UserModel();
        user.setSaldoMonetarioAtual(BigDecimal.valueOf(200));

        when(userService.findByLogin("john")).thenReturn(Optional.of(user));

        WithdrawRequestDTO request = new WithdrawRequestDTO(BigDecimal.valueOf(100));
        var response = depositCommandService.withdraw("john", request);

        assertEquals(new BigDecimal("100.00"), response.newBalance().setScale(2));
        assertEquals("Saque realizado com sucesso.", response.message());

        verify(transactionService).registrarTransacao(user, TransactionType.SAQUE, BigDecimal.valueOf(100));
        verify(transactionCacheService).addTransactionToCache(eq("john"), any());
    }
}
