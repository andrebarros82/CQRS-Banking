/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.config;

import br.com.cbcompany.cqrsbanking.cache.dto.TransactionCacheDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Classe de configuração do Redis para o projeto CQRS Banking.
 *
 * <p>
 * Esta configuração cria e disponibiliza um {@link RedisTemplate} que será
 * usado pelos serviços para armazenar e recuperar objetos
 * {@link TransactionCacheDTO} no Redis.</p>
 *
 * <p>
 * O uso de {@link GenericJackson2JsonRedisSerializer} com um
 * {@link ObjectMapper} configurado para suportar tipos Java 8 (como
 * {@link java.time.LocalDateTime}) garante a serialização correta das
 * transações que possuem campos de data/hora.</p>
 *
 * <p>
 * Chaves são armazenadas como Strings e valores são serializados em JSON,
 * promovendo interoperabilidade e legibilidade.</p>
 *
 * @author andre
 */
@Configuration
public class RedisConfig {

    /**
     * Cria e configura um {@link RedisTemplate} para gerenciar a comunicação
     * com o Redis.
     *
     * <p>
     * O template utiliza:</p>
     * <ul>
     * <li>{@link StringRedisSerializer} para serializar as chaves.</li>
     * <li>{@link GenericJackson2JsonRedisSerializer} para serializar os
     * valores, incluindo objetos do tipo {@link TransactionCacheDTO}.</li>
     * </ul>
     *
     * @param connectionFactory Fábrica de conexões do Redis, injetada
     * automaticamente pelo Spring.
     * @return Uma instância configurada de {@link RedisTemplate}.
     */
    @Bean
    public RedisTemplate<String, TransactionCacheDTO> redisTemplate(RedisConnectionFactory connectionFactory) {

        // Instância do RedisTemplate específica para chaves String e valores TransactionCacheDTO
        RedisTemplate<String, TransactionCacheDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Configuração do ObjectMapper para suportar serialização de tipos de data/hora do Java 8
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());       // Suporte a LocalDate, LocalDateTime, etc.
        mapper.findAndRegisterModules();                   // Registra módulos adicionais automaticamente

        // Serializer baseado em JSON com suporte a tipos Java modernos
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

        // Serialização das chaves como Strings
        template.setKeySerializer(new StringRedisSerializer());

        // Serialização dos valores (TransactionCacheDTO) em JSON
        template.setValueSerializer(serializer);

        // Serialização das chaves e valores de hashes em JSON
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        // Finaliza a configuração do template
        template.afterPropertiesSet();

        return template;
    }
}
