package org.rogeriocs.budgetinai.auth.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rogeriocs.budgetinai.auth.domain.UserRole;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long expirationMs = 3600000; // 1 hora

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(secretKey, expirationMs);
    }

    @Test
    @DisplayName("Deve gerar e validar token JWT com sucesso para um usuário")
    void shouldGenerateAndValidateTokenSuccessfully() {
        User user = new User(UUID.randomUUID(), "rogerio", "encoded_pass", UserRole.ROLE_USER);

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals("rogerio", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    @DisplayName("Deve invalidar token quando o usuário for diferente")
    void shouldBeInvalidForDifferentUser() {
        User user1 = new User(UUID.randomUUID(), "rogerio", "encoded_pass", UserRole.ROLE_USER);
        User user2 = new User(UUID.randomUUID(), "outro_user", "encoded_pass", UserRole.ROLE_USER);

        String token = jwtService.generateToken(user1);

        assertFalse(jwtService.isTokenValid(token, user2));
    }
}
