/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking.query.controller;

import br.com.cbcompany.cqrsbanking.query.dto.UserView;
import br.com.cbcompany.cqrsbanking.query.service.UserQueryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author andre
 */
@RestController
@RequestMapping("/api/queries/users")
public class UserQueryController {

    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<List<UserView>> getAllUsers() {
        return ResponseEntity.ok(userQueryService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserView> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userQueryService.getUserById(id));
    }
}
