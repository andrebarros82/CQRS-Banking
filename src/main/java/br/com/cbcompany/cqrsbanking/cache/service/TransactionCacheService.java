/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.service;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCacheDTO;
import java.util.List;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar o cache de transações de usuários utilizando Redis.
 *
 * <p>Este serviço permite adicionar novas transações no cache e recuperar o histórico
 * de transações de um usuário de forma eficiente, seguindo o padrão CQRS
 * para consultas rápidas.</p>
 *
 * <p>As transações são armazenadas em listas no Redis, com a chave no formato:
 * {@code transactions:{login}}, e o cache mantém apenas as 100 transações mais recentes.</p>
 * 
 * <p>O serviço utiliza {@link RedisTemplate} e {@link ListOperations} do Spring Data Redis
 * para manipular listas de objetos {@link TransactionCacheDTO}.</p>
 * 
 * @author andre
 */
@Service
public class TransactionCacheService {

    /**
     * Template do Redis utilizado para operações de armazenamento de transações.
     */
    private final RedisTemplate<String, TransactionCacheDTO> redisTemplate;

    /**
     * Operações de lista do Redis, usadas para adicionar e recuperar transações.
     */
    private final ListOperations<String, TransactionCacheDTO> listOps;

    /**
     * Construtor do serviço.
     *
     * @param redisTemplate Template Redis configurado para armazenar {@link TransactionCacheDTO}.
     */
    public TransactionCacheService(RedisTemplate<String, TransactionCacheDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    /**
     * Adiciona uma transação no cache de um usuário específico.
     *
     * <p>As transações são adicionadas no topo da lista e o cache mantém
     * apenas as 100 transações mais recentes, descartando as mais antigas.</p>
     *
     * @param login Login do usuário que realizou a transação.
     * @param transaction Objeto {@link TransactionCacheDTO} representando a transação.
     */
    public void addTransactionToCache(String login, TransactionCacheDTO transaction) {
        String key = "transactions:" + login;
        listOps.leftPush(key, transaction); // adiciona no topo da lista
        listOps.trim(key, 0, 99); // mantém apenas as 100 transações mais recentes
    }

    /**
     * Recupera o histórico de transações armazenadas no cache para um usuário.
     *
     * @param login Login do usuário cujas transações serão recuperadas.
     * @return Lista de objetos {@link TransactionCacheDTO} representando o histórico de transações.
     *         Retorna {@code null} se não houver transações para o usuário.
     */
    public List<TransactionCacheDTO> getTransactionsFromCache(String login) {
        String key = "transactions:" + login;
        return listOps.range(key, 0, -1); // retorna todas as transações
    }
}