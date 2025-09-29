/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.security;

import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por carregar detalhes de autenticação do usuário.
 *
 * <p>
 * Implementa a interface {@link UserDetailsService} do Spring Security, que é
 * utilizada durante o processo de autenticação para buscar informações do
 * usuário a partir do login fornecido.</p>
 *
 * <p>
 * Este serviço retorna uma instância de {@link CustomUserDetails} que encapsula
 * os dados do usuário necessários para autenticação e autorização.</p>
 *
 * Autor: andre
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repositório de usuários utilizado para buscar informações de login.
     */
    private final UserRepository userRepository;

    /**
     * Construtor do serviço.
     *
     * @param userRepository repositório de usuários
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega os detalhes do usuário pelo login fornecido.
     *
     * <p>
     * Este método é invocado automaticamente pelo Spring Security durante o
     * processo de autenticação. Caso o usuário não seja encontrado, lança uma
     * {@link UsernameNotFoundException}.</p>
     *
     * @param login login do usuário
     * @return objeto {@link UserDetails} contendo informações do usuário
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserModel user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
        return new CustomUserDetails(user);
    }
}
