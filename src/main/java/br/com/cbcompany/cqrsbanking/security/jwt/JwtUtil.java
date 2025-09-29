/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utilitário para geração, validação e extração de informações de tokens JWT.
 *
 * <p>
 * Esta classe encapsula a criação de tokens JWT, extração de claims e
 * verificação de validade.</p>
 *
 * Autor: andre
 */
@Component
public class JwtUtil {

    /**
     * Chave secreta utilizada para assinar os tokens JWT. É carregada do
     * arquivo de propriedades (application.properties / application.yml).
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Tempo de expiração do token em milissegundos.
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Constrói a chave de assinatura HMAC usando a secretKey.
     *
     * @return SecretKey utilizada para assinar/verificar tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Gera um token JWT para o usuário especificado.
     *
     * @param username nome de usuário para o qual o token será gerado
     * @return token JWT assinado
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrai o username (subject) de um token JWT.
     *
     * @param token token JWT
     * @return nome de usuário presente no token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrai a data de expiração de um token JWT.
     *
     * @param token token JWT
     * @return data de expiração
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrai um claim específico de um token JWT usando uma função de
     * resolução.
     *
     * @param <T> tipo do claim
     * @param token token JWT
     * @param claimsResolver função que extrai o claim desejado
     * @return valor do claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    /**
     * Verifica se o token JWT é válido para o usuário especificado.
     *
     * @param token token JWT
     * @param username nome de usuário
     * @return true se o token for válido e não expirado, false caso contrário
     */
    public boolean isTokenValid(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token token JWT
     * @return true se o token estiver expirado, false caso contrário
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
