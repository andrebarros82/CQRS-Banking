/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.repository;

import br.com.cbcompany.cqrsbanking.model.UserModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author andre
 */
public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByLogin(String login);

    Optional<UserModel> findByCpf(String cpf);

    boolean existsByLogin(String login);

    boolean existsByCpf(String cpf);
}
