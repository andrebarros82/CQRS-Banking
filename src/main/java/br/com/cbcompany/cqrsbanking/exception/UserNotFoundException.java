/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.exception;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário que não existe
 * no sistema.
 *
 * <p>
 * Essa exceção é utilizada principalmente pelos serviços que buscam usuários
 * pelo login ou identificador único, indicando que o usuário solicitado não foi
 * encontrado.</p>
 *
 * Autor: andre
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro.
     *
     * @param message mensagem descritiva da exceção, indicando qual usuário não
     * foi encontrado
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
