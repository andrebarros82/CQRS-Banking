/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cbcompany.cqrsbanking;

import br.com.cbcompany.cqrsbanking.command.dto.RegisterUserRequestDTO;
import br.com.cbcompany.cqrsbanking.command.service.UserCommandService;
import br.com.cbcompany.cqrsbanking.model.UserModel;
import br.com.cbcompany.cqrsbanking.repository.UserRepository;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author andre
 */
class UserCommandServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserCommandService userCommandService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userCommandService = new UserCommandService(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUser_Success() {
        RegisterUserRequestDTO dto = new RegisterUserRequestDTO(
                "John Doe",
                "12345678900",
                "john",
                "senha123",
                BigDecimal.valueOf(100)
        );

        when(userRepository.existsByLogin("john")).thenReturn(false);
        when(userRepository.existsByCpf("12345678900")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("encrypted");
        when(userRepository.save(any(UserModel.class))).thenAnswer(i -> i.getArguments()[0]);

        UserModel user = userCommandService.registerUser(dto);

        assertEquals("John Doe", user.getNomeCompleto());
        assertEquals("encrypted", user.getSenha());
        assertEquals(BigDecimal.valueOf(100), user.getSaldoMonetarioAtual());
    }

    @Test
    void testRegisterUser_LoginExists() {
        when(userRepository.existsByLogin("john")).thenReturn(true);

        RegisterUserRequestDTO dto = new RegisterUserRequestDTO(
                "John Doe",
                "12345678900",
                "john",
                "senha123",
                BigDecimal.valueOf(100)
        );

        assertThrows(IllegalArgumentException.class, () -> userCommandService.registerUser(dto));
    }

    @Test
    void testRegisterUser_CpfExists() {
        when(userRepository.existsByLogin("john")).thenReturn(false);
        when(userRepository.existsByCpf("12345678900")).thenReturn(true);

        RegisterUserRequestDTO dto = new RegisterUserRequestDTO(
                "John Doe",
                "12345678900",
                "john",
                "senha123",
                BigDecimal.valueOf(100)
        );

        assertThrows(IllegalArgumentException.class, () -> userCommandService.registerUser(dto));
    }
}
