/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.security.config;

import br.com.cbcompany.cqrsbanking.security.CustomUserDetailsService;
import br.com.cbcompany.cqrsbanking.security.jwt.JwtAuthenticationFilter;
import br.com.cbcompany.cqrsbanking.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração de segurança da aplicação.
 *
 * <p>
 * Configura a autenticação e autorização utilizando Spring Security, incluindo
 * JWT, filtros de autenticação e codificação de senha.</p>
 *
 * <p>
 * Autor: andre</p>
 */
@Configuration
public class SecurityConfig {

    /**
     * Utilitário para geração e validação de tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Serviço customizado de carregamento de detalhes do usuário.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Construtor da classe de configuração de segurança.
     *
     * @param jwtUtil utilitário JWT
     * @param userDetailsService serviço customizado de detalhes do usuário
     */
    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura a cadeia de filtros de segurança e autorizações.
     *
     * <p>
     * Desativa CSRF, permite acesso a URLs públicas (H2, Swagger, autenticação)
     * e adiciona o filtro JWT antes do filtro padrão de autenticação.</p>
     *
     * @param http objeto de configuração do HttpSecurity
     * @return cadeia de filtros de segurança
     * @throws Exception caso ocorra erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/commands/users/**",
                        "/api/auth/**",
                        "/h2-console/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean do filtro de autenticação JWT.
     *
     * @return instância de {@link JwtAuthenticationFilter}
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    /**
     * Configura o provedor de autenticação usando
     * {@link DaoAuthenticationProvider} com serviço customizado de detalhes do
     * usuário e codificador de senha.
     *
     * @return provedor de autenticação
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean do {@link AuthenticationManager} usado pelo Spring Security.
     *
     * @param config configuração de autenticação
     * @return gerenciador de autenticação
     * @throws Exception caso ocorra erro
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean do codificador de senha usando {@link BCryptPasswordEncoder}.
     *
     * @return codificador de senha
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
