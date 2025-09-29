/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author andre
 */
public record LoginRequestDTO(@NotBlank(message = "Login é obrigatório")
        String login,
        @NotBlank(message = "Senha é obrigatória")
        String password) {

}
