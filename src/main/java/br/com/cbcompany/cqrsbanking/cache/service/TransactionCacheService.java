/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.service;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCache;
import java.util.List;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class TransactionCacheService {

    private final RedisTemplate<String, TransactionCache> redisTemplate;
    private final ListOperations<String, TransactionCache> listOps;

    public TransactionCacheService(RedisTemplate<String, TransactionCache> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    public void addTransactionToCache(String login, TransactionCache transaction) {
        String key = "transactions:" + login;
        listOps.leftPush(key, transaction); // adiciona no topo da lista
        listOps.trim(key, 0, 99); // mantém apenas as 100 transações mais recentes
    }

    public List<TransactionCache> getTransactionsFromCache(String login) {
        String key = "transactions:" + login;
        return listOps.range(key, 0, -1);
    }
}
