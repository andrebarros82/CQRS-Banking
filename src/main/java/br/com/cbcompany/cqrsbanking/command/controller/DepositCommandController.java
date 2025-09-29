/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.controller;

import br.com.cbcompany.cqrsbanking.command.service.DepositCommandService;
import br.com.cbcompany.cqrsbanking.command.dto.DepositRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.DepositResponseDTO;
import br.com.cbcompany.cqrsbanking.command.dto.PayBillRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.PayBillResponseDTO;
import br.com.cbcompany.cqrsbanking.command.dto.WithdrawRequestDTO;
import br.com.cbcompany.cqrsbanking.command.dto.WithdrawResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador responsável por expor os endpoints de comandos relacionados a operações financeiras do usuário,
 * como depósito, saque e pagamento de contas.
 *
 * <p>Este controlador segue o padrão CQRS, delegando a execução dos comandos ao
 * {@link DepositCommandService}, enquanto as consultas são realizadas por outros componentes do sistema.</p>
 *
 * <p>Todos os endpoints exigem autenticação JWT para identificar o usuário.</p>
 * 
 * @author andre
 */
@RestController
@RequestMapping("/api/commands/users")
@Tag(name = "User Financial Commands", description = "Operações financeiras de usuários (Depósito, Saque, Pagamento de contas)")
public class DepositCommandController {

    /**
     * Serviço responsável por executar os comandos financeiros.
     */
    private final DepositCommandService depositCommandService;

    /**
     * Construtor do controlador.
     *
     * @param depositCommandService serviço de comandos financeiros.
     */
    public DepositCommandController(DepositCommandService depositCommandService) {
        this.depositCommandService = depositCommandService;
    }

    /**
     * Realiza a operação de depósito de um valor monetário na conta do usuário autenticado.
     *
     * @param request objeto contendo o valor do depósito.
     * @param authentication contexto de autenticação que fornece o login do usuário.
     * @return objeto {@link DepositResponseDTO} com os detalhes da transação.
     */
    @Operation(
            summary = "Realiza um depósito",
            description = "Permite que o usuário autenticado deposite um valor em sua conta.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = DepositResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo inconsistente"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/deposit")
    public DepositResponseDTO deposit(
            @Valid
            @RequestBody(
                    description = "Dados necessários para realizar um depósito",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DepositRequestDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody DepositRequestDTO request,
            Authentication authentication) {

        String login = authentication.getName();
        return depositCommandService.deposit(login, request);
    }

    /**
     * Realiza a operação de saque de um valor monetário da conta do usuário autenticado.
     *
     * @param request objeto contendo o valor a ser sacado.
     * @param authentication contexto de autenticação que fornece o login do usuário.
     * @return objeto {@link WithdrawResponseDTO} com os detalhes da transação.
     */
    @Operation(
            summary = "Realiza um saque",
            description = "Permite que o usuário autenticado saque um valor de sua conta.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Saque realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = WithdrawResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/withdraw")
    public WithdrawResponseDTO withdraw(
            @Valid
            @RequestBody(
                    description = "Dados necessários para realizar um saque",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WithdrawRequestDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody WithdrawRequestDTO request,
            Authentication authentication) {

        String login = authentication.getName();
        return depositCommandService.withdraw(login, request);
    }

    /**
     * Realiza a operação de pagamento de contas utilizando o saldo do usuário autenticado.
     *
     * @param request objeto contendo as informações da conta a ser paga.
     * @param authentication contexto de autenticação que fornece o login do usuário.
     * @return objeto {@link PayBillResponseDTO} com os detalhes da transação.
     */
    @Operation(
            summary = "Realiza um pagamento de conta",
            description = "Permite que o usuário autenticado pague uma conta utilizando seu saldo disponível.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pagamento realizado com sucesso",
                            content = @Content(schema = @Schema(implementation = PayBillResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
                    @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping("/payBill")
    public PayBillResponseDTO payBill(
            @Valid
            @RequestBody(
                    description = "Dados necessários para realizar o pagamento de uma conta",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PayBillRequestDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody PayBillRequestDTO request,
            Authentication authentication) {

        String login = authentication.getName();
        return depositCommandService.payBill(login, request);
    }
}