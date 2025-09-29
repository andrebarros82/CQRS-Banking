/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 *
 * <p>
 * Esta classe cria um bean OpenAPI que fornece informações básicas da API, como
 * título, descrição e versão, permitindo que o Springdoc gere a interface
 * Swagger automaticamente.</p>
 *
 * Autor: andre
 */
@Configuration
public class SwaggerConfig {

    /**
     * Cria e configura o bean OpenAPI com informações gerais da API.
     *
     * @return Objeto OpenAPI configurado com título, descrição e versão
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("CQRS Banking API")
                        .description("API para operações bancárias com CQRS, JWT e Spring Boot 3.5.6")
                        .version("1.0.0")); 
    }
}
