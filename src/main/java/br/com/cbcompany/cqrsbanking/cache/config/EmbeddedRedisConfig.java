/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.cache.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

/**
 * Configuração de um servidor Redis embarcado (in-memory) para ambientes
 * locais.
 *
 * <p>
 * Essa configuração permite executar a aplicação sem depender de um servidor
 * Redis externo durante o desenvolvimento, simplificando a configuração do
 * ambiente.</p>
 *
 * <p>
 * A anotação {@link Profile} garante que o Redis embarcado seja iniciado apenas
 * quando o perfil <strong>local</strong> estiver ativo.</p>
 *
 * <p>
 * O ciclo de vida do Redis embarcado é controlado pelos métodos anotados com
 * {@link PostConstruct} e {@link PreDestroy}, que garantem a inicialização e
 * finalização corretas do serviço.</p>
 *
 * <pre>
 * Exemplos de ativação do perfil local:
 *
 * # application.properties
 * spring.profiles.active=local
 * </pre>
 *
 * @author andre
 */
@Configuration
@Profile("local") // Esta configuração só é aplicada quando o perfil "local" está ativo
public class EmbeddedRedisConfig {

    /**
     * Instância do servidor Redis embarcado. É inicializada quando a aplicação
     * é iniciada e parada na finalização.
     */
    private RedisServer redisServer;

    /**
     * Inicia o servidor Redis embarcado.
     *
     * <p>
     * O método é executado automaticamente após a injeção de dependências
     * (graças à anotação {@link PostConstruct}).</p>
     *
     * @throws IOException se houver falha ao iniciar o servidor Redis.
     */
    @PostConstruct
    public void startRedis() throws IOException {
        // Inicia o servidor Redis embarcado na porta padrão 6379
        redisServer = new RedisServer(6379);
        redisServer.start();
        System.out.println("Redis embarcado iniciado na porta 6379 (perfil local).");
    }

    /**
     * Para o servidor Redis embarcado.
     *
     * <p>
     * O método é chamado automaticamente antes do encerramento da aplicação
     * (graças à anotação {@link PreDestroy}).</p>
     */
    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            System.out.println("Redis embarcado foi finalizado.");
        }
    }
}
