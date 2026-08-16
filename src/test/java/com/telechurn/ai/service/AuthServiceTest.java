package com.telechurn.ai.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.telechurn.ai.dto.RegisterRequest;
import com.telechurn.ai.entity.User;
import com.telechurn.ai.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void registerHashesPasswordAndPersistsUser() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        when(userRepository.findByEmail("analyst@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthService service = new AuthService(userRepository, passwordEncoder);
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Analyst User");
        request.setEmail("analyst@example.com");
        request.setPassword("Password123");
        request.setConfirmPassword("Password123");
        request.setRole(User.Role.ANALYST);

        User user = service.register(request);

        assertThat(user.getPassword()).isNotEqualTo("Password123");
        assertThat(passwordEncoder.matches("Password123", user.getPassword())).isTrue();
        assertThat(user.getRole()).isEqualTo(User.Role.ANALYST);
    }
}
