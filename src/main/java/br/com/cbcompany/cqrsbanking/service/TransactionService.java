/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.service;

import br.com.cbcompany.cqrsbanking.model.TransactionModel;
import br.com.cbcompany.cqrsbanking.model.TransactionType;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelo registro e gerenciamento de transações financeiras.
 *
 * <p>
 * Esta classe encapsula a lógica de persistência das transações na base de
 * dados através do repositório {@link TransactionRepository}.</p>
 *
 * Autor: andre
 */
@Service
public class TransactionService {

    /**
     * Repositório para persistência das transações.
     */
    private final TransactionRepository transactionRepository;

    /**
     * Construtor que injeta o repositório de transações.
     *
     * @param transactionRepository repositório para persistir transações
     */
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Registra uma nova transação para um usuário.
     *
     * <p>
     * Este método cria um {@link TransactionModel}, define o usuário, tipo de
     * transação, valor e data/hora atual, e persiste o registro no banco de
     * dados.</p>
     *
     * @param user usuário relacionado à transação
     * @param tipo tipo da transação (ex: DEPOSITO, SAQUE, PAGAMENTO)
     * @param valor valor monetário da transação
     */
    public void registrarTransacao(UserModel user, TransactionType tipo, BigDecimal valor) {
        TransactionModel transaction = new TransactionModel();
        transaction.setUser(user);
        transaction.setTipo(tipo);
        transaction.setValor(valor);
        transaction.setDataHora(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}
