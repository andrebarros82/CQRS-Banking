/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.command.service;

import br.com.cbcompany.cqrsbanking.command.dto.RegisterUserCommand;
import br.com.cbcompany.cqrsbanking.model.User;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterUserCommand cmd) {
        if (userRepository.existsByLogin(cmd.login())) {
            throw new IllegalArgumentException("Login já está em uso.");
        }
        if (userRepository.existsByCpf(cmd.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        User user = new User();

        user.setNomeCompleto(cmd.nomeCompleto());
        user.setCpf(cmd.cpf());
        user.setLogin(cmd.login());
        user.setSenha(passwordEncoder.encode(cmd.senha()));
        user.setSaldoMonetarioAtual(cmd.saldoMonetarioAtual());

        return userRepository.save(user);
    }
}
