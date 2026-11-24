package org.rogeriocs.budgetinai.auth.infrastructure.http;

import org.rogeriocs.budgetinai.auth.domain.UserRole;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.entity.User;
import org.rogeriocs.budgetinai.auth.infrastructure.persistence.repository.UserRepository;
import org.rogeriocs.budgetinai.auth.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                UserRole.ROLE_USER
        );
        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(jwtToken, "Bearer", jwtService.getExpirationMs(), savedUser.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(
                new AuthResponse(jwtToken, "Bearer", jwtService.getExpirationMs(), user.getUsername())
        );
    }

    public record LoginRequest(String username, String password) {}

    public record RegisterRequest(String username, String password) {}

    public record AuthResponse(String token, String type, long expiresIn, String username) {}
}
