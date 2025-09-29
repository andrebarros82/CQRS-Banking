/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.service;

import br.com.cbcompany.cqrsbanking.command.dto.RegisterUserRequestDTO;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelos comandos relacionados aos usuários, como cadastro.
 *
 * <p>
Seguindo o padrão CQRS, esta classe concentra operações de escrita (comandos)
para o agregado UserModel.</p>
 *
 * <p>
 * Inclui validações de unicidade para login e CPF, além de codificação segura
 * da senha.</p>
 *
 * Autor: andre
 */
@Service
public class UserCommandService {

    /**
     * Repositório de persistência de usuários
     */
    private final UserRepository userRepository;

    /**
     * Encoder para armazenar senhas de forma segura
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Construtor que injeta as dependências necessárias para o serviço.
     *
     * @param userRepository Repositório de usuários
     * @param passwordEncoder Encoder de senhas
     */
    public UserCommandService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Realiza o registro de um novo usuário.
     * <p>
     * Valida se o login e CPF já estão em uso, codifica a senha e persiste o
     * usuário no banco de dados.</p>
     *
     * @param cmd DTO com os dados necessários para registrar o usuário
     * @return Usuário persistido no banco
     * @throws IllegalArgumentException se o login ou CPF já estiverem
     * cadastrados
     */
    public UserModel registerUser(RegisterUserRequestDTO cmd) {
        if (userRepository.existsByLogin(cmd.login())) {
            throw new IllegalArgumentException("Login já está em uso.");
        }
        if (userRepository.existsByCpf(cmd.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        UserModel user = new UserModel();
        user.setNomeCompleto(cmd.nomeCompleto());
        user.setCpf(cmd.cpf());
        user.setLogin(cmd.login());
        user.setSenha(passwordEncoder.encode(cmd.senha())); // codifica senha
        user.setSaldoMonetarioAtual(cmd.saldoMonetarioAtual());

        return userRepository.save(user);
    }
}
