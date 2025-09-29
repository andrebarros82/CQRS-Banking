/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manipulador global de exceções para a API REST.
 *
 * <p>Esta classe centraliza o tratamento de exceções lançadas pelos controllers,
 * fornecendo respostas HTTP padronizadas com informações detalhadas sobre o erro.</p>
 *
 * Autor: andre
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de argumentos em requisições HTTP.
     *
     * <p>Quando uma requisição falha nas validações de Bean Validation,
     * este método constrói um corpo de resposta detalhado contendo:
     * <ul>
     *     <li>timestamp: data e hora do erro</li>
     *     <li>status: código HTTP (400)</li>
     *     <li>error: descrição do tipo de erro</li>
     *     <li>message: mensagem geral do erro</li>
     *     <li>fields: mapa contendo o nome do campo e a mensagem de validação</li>
     * </ul>
     * </p>
     *
     * @param ex a exceção lançada durante a validação de argumentos
     * @return {@link ResponseEntity} com corpo detalhado e status HTTP 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        body.put("message", "Erro de validação nos campos");
        body.put("fields", fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Trata exceções do tipo {@link UserNotFoundException}.
     *
     * @param ex a exceção lançada quando um usuário não é encontrado
     * @return {@link ResponseEntity} com corpo detalhado e status HTTP 404 (Not Found)
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Trata exceções do tipo {@link IllegalArgumentException}.
     *
     * @param ex a exceção lançada em caso de argumentos inválidos ou inconsistentes
     * @return {@link ResponseEntity} com corpo detalhado e status HTTP 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Trata quaisquer exceções não tratadas anteriormente.
     *
     * @param ex a exceção genérica capturada
     * @return {@link ResponseEntity} com corpo detalhado e status HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}