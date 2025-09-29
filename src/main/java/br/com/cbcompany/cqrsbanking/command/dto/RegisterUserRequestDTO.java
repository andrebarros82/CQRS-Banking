/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) utilizado para o registro de um novo usuário no
 * sistema.
 * <p>
 * Fornece os dados necessários para cadastrar o usuário, incluindo informações
 * pessoais e saldo inicial.
 * </p>
 *
 * <p>
 * Exemplo de requisição JSON:</p>
 * <pre>
 * {
 *   "nomeCompleto": "João da Silva",
 *   "cpf": "12345678901",
 *   "login": "joaosilva",
 *   "senha": "senhaSegura123",
 *   "saldoMonetarioAtual": 1000.00
 * }
 * </pre>
 *
 * <p>
 * <b>Validações aplicadas:</b></p>
 * <ul>
 * <li>{@code @NotBlank} – garante que o campo não esteja nulo nem vazio.</li>
 * <li>{@code @Pattern} – usado para restringir o formato do CPF.</li>
 * <li>{@code @Size} – usado para impor tamanho mínimo/máximo de campos como
 * senha.</li>
 * <li>{@code @NotNull} – aplicado ao saldo inicial (quando obrigatório).</li>
 * </ul>
 *
 * @author andre
 */
@Schema(description = "Objeto de requisição para o registro de um novo usuário.")
public record RegisterUserRequestDTO(
        /**
         * Nome completo do usuário.
         */
        @Schema(
                description = "Nome completo do usuário.",
                example = "João da Silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome completo é obrigatório.")
        String nomeCompleto,
        /**
         * CPF do usuário. Deve conter apenas números, sem pontos ou traços.
         */
        @Schema(
                description = "CPF do usuário (somente números, sem pontos ou traços).",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(
                regexp = "\\d{11}",
                message = "O CPF deve conter exatamente 11 dígitos numéricos."
        )
        String cpf,
        /**
         * Login do usuário para autenticação.
         */
        @Schema(
                description = "Login que o usuário utilizará para acessar o sistema.",
                example = "joaosilva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O login é obrigatório.")
        String login,
        /**
         * Senha do usuário. Recomenda-se aplicar regras de segurança
         * (comprimento mínimo e complexidade).
         */
        @Schema(
                description = "Senha de acesso do usuário. Deve ter pelo menos 6 caracteres.",
                example = "senhaSegura123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha,
        /**
         * Saldo monetário inicial do usuário. Não pode ser nulo. Pode ser zero
         * ou positivo.
         */
        @Schema(
                description = "Saldo monetário inicial do usuário.",
                example = "1000.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O saldo monetário inicial é obrigatório.")
        @Min(value = 1, message = "O saldo monetário inicial mínimo é 1.")
        BigDecimal saldoMonetarioAtual
        ) {

}
