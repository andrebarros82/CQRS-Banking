/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.service;

import br.com.cbcompany.cqrsbanking.cache.dto.HistoricoDTO;
import br.com.cbcompany.cqrsbanking.cache.dto.HistoricoResponseDTO;
import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCacheDTO;
import br.com.cbcompany.cqrsbanking.cache.service.TransactionCacheService;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por consultar o histórico de transações de um usuário.
 *
 * <p>
 * Este serviço acessa o cache Redis para obter as transações recentes de cada
 * usuário e monta um DTO de resposta contendo o saldo atual e o histórico de
 * transações.</p>
 *
 * <p>
 * As transações retornadas incluem depósitos, saques e pagamentos, já
 * formatadas para exibição.</p>
 *
 * @author andre
 */
@Service
public class HistoricoQueryService {

    /**
     * Serviço responsável pelo acesso ao cache Redis para transações.
     */
    private final TransactionCacheService transactionCacheService;

    /**
     * Repositório de usuários para obter dados do usuário.
     */
    private final UserRepository userRepository;

    /**
     * Construtor do serviço.
     *
     * @param transactionCacheService serviço de cache de transações
     * @param userRepository repositório de usuários
     */
    public HistoricoQueryService(TransactionCacheService transactionCacheService,
            UserRepository userRepository) {
        this.transactionCacheService = transactionCacheService;
        this.userRepository = userRepository;
    }

    /**
     * Obtém o histórico de transações de um usuário pelo login.
     *
     * <p>
     * O histórico inclui todas as transações armazenadas no cache Redis e
     * retorna o saldo total atual do usuário.</p>
     *
     * @param login login do usuário
     * @return DTO contendo o saldo atual e a lista de transações
     */
    public HistoricoResponseDTO getHistorico(String login) {
        // Recupera o usuário pelo login
        UserModel user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Recupera as transações do cache (podem ser armazenadas como LinkedHashMap)
        List<?> rawTransactions = transactionCacheService.getTransactionsFromCache(login);

        // Configura ObjectMapper para conversão de datas no formato JavaTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Converte cada registro bruto em TransactionCacheDTO
        List<TransactionCacheDTO> transactions = rawTransactions.stream()
                .map(t -> objectMapper.convertValue(t, TransactionCacheDTO.class))
                .collect(Collectors.toList());

        // Formato padrão para exibição da data e hora das transações
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Converte as transações para o DTO de histórico
        List<HistoricoDTO> historico = transactions.stream()
                .map(t -> new HistoricoDTO(
                t.tipo().toLowerCase(), // tipo da transação (depósito, saque, pagamento)
                t.valor().toString(), // valor da transação em string
                t.dataHora().format(formatter) // data e hora formatadas
        ))
                .collect(Collectors.toList());

        // Retorna o DTO com saldo atual e histórico de transações
        return new HistoricoResponseDTO(
                user.getSaldoMonetarioAtual().toString(),
                historico
        );
    }
}
