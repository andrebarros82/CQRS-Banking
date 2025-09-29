/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.service;

import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.query.dto.UserViewResponseDTO;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por consultas relacionadas a usuários (Query).
 *
 * <p>
 * Este serviço fornece operações de leitura sobre a entidade {@link UserModel},
 * retornando DTOs apropriados para exibição ou consumo via API.</p>
 *
 * <p>
 * Seguindo o padrão CQRS, esta classe lida apenas com consultas
 * (read-only).</p>
 *
 * @author andre
 */
@Service
public class UserQueryService {

    /**
     * Repositório de usuários utilizado para realizar buscas na base de dados.
     */
    private final UserRepository userRepository;

    /**
     * Construtor do serviço.
     *
     * @param userRepository repositório de usuários
     */
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtém os dados de um usuário pelo seu identificador (ID).
     *
     * <p>
     * Retorna um DTO {@link UserViewResponseDTO} com os campos relevantes do
     * usuário. Caso o usuário não seja encontrado, uma {@link RuntimeException}
     * é lançada.</p>
     *
     * @param id identificador do usuário
     * @return DTO contendo informações do usuário
     * @throws RuntimeException se o usuário não for encontrado
     */
    public UserViewResponseDTO getUserById(Long id) {
        UserModel u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserViewResponseDTO(
                u.getId(),
                u.getNomeCompleto(),
                u.getCpf(),
                u.getLogin(),
                u.getSaldoMonetarioAtual()
        );
    }
}
