package org.rogeriocs.budgetinai.auth.infrastructure.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rogeriocs.budgetinai.auth.domain.UserRole;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.repository.UserRepository;
import org.rogeriocs.budgetinai.auth.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.docker.compose.skip.in-tests=false",
        "spring.ai.openai.api-key=test-key-for-context-load"
})
class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/login deve retornar 200 e token JWT para credenciais válidas")
    void shouldLoginSuccessfully() throws Exception {
        String username = "auth_test_user_" + UUID.randomUUID();
        userRepository.save(new User(username, passwordEncoder.encode("secret123"), UserRole.ROLE_USER));

        String body = """
                {
                    "username": "%s",
                    "password": "secret123"
                }
                """.formatted(username);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    @DisplayName("Requisições para rotas protegidas sem token devem retornar 403 Forbidden")
    void shouldDenyUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/transactions/GROCERIES"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Requisições com token válido devem ser aceitas")
    void shouldAcceptAuthenticatedRequests() throws Exception {
        String username = "valid_token_user_" + UUID.randomUUID();
        User user = new User(username, passwordEncoder.encode("pass"), UserRole.ROLE_USER);
        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get("/transactions/GROCERIES")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Criação de transação autenticada deve vincular o UUID do usuário logado")
    void shouldAssociateTransactionWithUserUUID() throws Exception {
        String username = "tx_user_" + UUID.randomUUID();
        User user = new User(username, passwordEncoder.encode("pass"), UserRole.ROLE_USER);
        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        String txPayload = """
                {
                    "description": "Café",
                    "category": "GROCERIES",
                    "amount": 1000
                }
                """;

        mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(txPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.owner.name").value(user.getUsername()));
    }
}
