/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.repository;

import br.com.cbcompany.cqrsbanking.model.TransactionModel;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author andre
 */
public interface TransactionRepository extends JpaRepository<TransactionModel, Long> {

    List<TransactionModel> findByUserOrderByDataHoraDesc(UserModel user);
}
