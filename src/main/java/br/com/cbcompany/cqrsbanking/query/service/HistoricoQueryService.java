/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.service;

import br.com.cbcompany.cqrsbanking.cache.dto.Historico;
import br.com.cbcompany.cqrsbanking.cache.dto.HistoricoResponseDTO;
import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCache;
import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import br.com.cbcompany.cqrsbanking.model.User;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class HistoricoQueryService {

    private final TransactionCacheService transactionCacheService;
    private final UserRepository userRepository;

    public HistoricoQueryService(TransactionCacheService transactionCacheService,
            UserRepository userRepository) {
        this.transactionCacheService = transactionCacheService;
        this.userRepository = userRepository;
    }

    public HistoricoResponseDTO getHistorico(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Recupera as transações do cache (podem ser LinkedHashMap)
        List<?> rawTransactions = transactionCacheService.getTransactionsFromCache(login);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Converte cada LinkedHashMap em TransactionCache
        List<TransactionCache> transactions = rawTransactions.stream()
                .map(t -> objectMapper.convertValue(t, TransactionCache.class))
                .collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        List<Historico> historico = transactions.stream()
                .map(t -> new Historico(
                t.tipo().toLowerCase(),
                t.valor().toString(),
                t.dataHora().format(formatter)
        ))
                .collect(Collectors.toList());

        return new HistoricoResponseDTO(
                user.getSaldoMonetarioAtual().toString(),
                historico
        );
    }
}
