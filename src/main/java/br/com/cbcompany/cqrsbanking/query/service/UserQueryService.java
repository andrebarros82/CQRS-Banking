/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.service;

import br.com.cbcompany.cqrsbanking.model.User;
import br.com.cbcompany.cqrsbanking.query.dto.UserView;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author andre
 */
@Service
public class UserQueryService {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserView> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserView(
                u.getId(),
                u.getNomeCompleto(),
                u.getCpf(),
                u.getLogin(),
                u.getSaldoMonetarioAtual()))
                .toList();
    }

    public UserView getUserById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserView(u.getId(), u.getNomeCompleto(), u.getCpf(), u.getLogin(), u.getSaldoMonetarioAtual());
    }
}
