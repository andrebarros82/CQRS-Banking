/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.service;

import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerenciar usuários.
 *
 * <p>
 * Esta classe encapsula a lógica de acesso e manipulação de dados do usuário
 * através do repositório {@link UserRepository}.</p>
 *
 * Autor: andre
 */
@Service
public class UserService {

    /**
     * Repositório para persistência e consulta de usuários.
     */
    private final UserRepository userRepository;

    /**
     * Construtor que injeta o repositório de usuários.
     *
     * @param userRepository repositório para gerenciar usuários
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca um usuário pelo login.
     *
     * @param login login do usuário
     * @return Optional contendo o usuário, se encontrado
     */
    public Optional<UserModel> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    /**
     * Busca um usuário pelo CPF.
     *
     * @param cpf CPF do usuário
     * @return Optional contendo o usuário, se encontrado
     */
    public Optional<UserModel> findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }

    /**
     * Verifica se já existe um usuário com o login informado.
     *
     * @param login login a ser verificado
     * @return true se o login já existir, false caso contrário
     */
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    /**
     * Verifica se já existe um usuário com o CPF informado.
     *
     * @param cpf CPF a ser verificado
     * @return true se o CPF já existir, false caso contrário
     */
    public boolean existsByCpf(String cpf) {
        return userRepository.existsByCpf(cpf);
    }

    /**
     * Salva ou atualiza um usuário no banco de dados.
     *
     * @param user usuário a ser salvo
     * @return usuário persistido
     */
    public UserModel save(UserModel user) {
        return userRepository.save(user);
    }
}
