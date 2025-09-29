/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.security.jwt;

import br.com.cbcompany.cqrsbanking.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autenticação JWT que intercepta cada requisição HTTP e valida
 * o token JWT presente no header "Authorization".
 *
 * <p>Se o token for válido, o usuário é autenticado no contexto do Spring Security.</p>
 *
 * Autor: andre
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Utilitário para manipulação e validação de tokens JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     * Serviço customizado de carregamento de detalhes do usuário.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Construtor do filtro JWT.
     *
     * @param jwtUtil utilitário de JWT
     * @param userDetailsService serviço customizado de detalhes do usuário
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método que intercepta cada requisição HTTP para validar o token JWT.
     *
     * <p>Se o header "Authorization" contiver um token Bearer válido, autentica
     * o usuário no contexto do Spring Security.</p>
     *
     * @param request requisição HTTP
     * @param response resposta HTTP
     * @param filterChain cadeia de filtros
     * @throws ServletException exceção de servlet
     * @throws IOException exceção de I/O
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // Verifica se o header contém o token Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                logger.warn("Token JWT inválido ou expirado: " + e.getMessage());
            }
        }

        // Autentica o usuário caso não esteja autenticado ainda
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwtToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken
                        = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}