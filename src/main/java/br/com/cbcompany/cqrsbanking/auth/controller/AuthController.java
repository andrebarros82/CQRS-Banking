/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.auth.controller;

import br.com.cbcompany.cqrsbanking.auth.dto.ErrorAuthResponseDTO;
import br.com.cbcompany.cqrsbanking.auth.dto.JwtResponseDTO;
import br.com.cbcompany.cqrsbanking.auth.dto.LoginRequestDTO;
import br.com.cbcompany.cqrsbanking.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável pela autenticação de usuários e geração de tokens
 * JWT.
 *
 * <p>
 * Este controlador faz parte do fluxo de autenticação do sistema CQRS Banking e
 * expõe o endpoint para login.</p>
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas à autenticação de usuários")
public class AuthController {

    /**
     * Gerenciador de autenticação do Spring Security responsável por autenticar
     * as credenciais fornecidas pelo usuário.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Componente utilitário responsável por gerar e validar tokens JWT.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Realiza a autenticação do usuário com base no login e senha fornecidos,
     * gerando um token JWT em caso de sucesso.
     *
     * @param loginRequest Objeto contendo as credenciais do usuário (login e
     * senha).
     * @return Objeto {@link JwtResponseDTO} contendo o token JWT.
     * @throws RuntimeException Caso as credenciais sejam inválidas.
     */
    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica o usuário com as credenciais fornecidas e retorna um token JWT para uso nas demais operações.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Autenticação bem-sucedida",
                        content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Credenciais inválidas",
                        content = @Content(schema = @Schema(implementation = ErrorAuthResponseDTO.class))
                )
            }
    )
    @PostMapping("/login")
    public JwtResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequest) {

        // Autentica as credenciais do usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.login(),
                        loginRequest.password()
                )
        );

        // Gera o token JWT
        String token = jwtUtil.generateToken(loginRequest.login());

        return new JwtResponseDTO(token);
    }
}
