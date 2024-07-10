package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.AuthDTO;
import pl.pomoku.inventorymanagementapp.dto.request.RegisterUserDTO;
import pl.pomoku.inventorymanagementapp.dto.response.AuthResponse;
import pl.pomoku.inventorymanagementapp.entity.ConfirmationToken;
import pl.pomoku.inventorymanagementapp.entity.Role;
import pl.pomoku.inventorymanagementapp.entity.Token;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.exception.auth.AccountAlreadyConfirmException;
import pl.pomoku.inventorymanagementapp.exception.auth.InvalidPasswordOrEmailException;
import pl.pomoku.inventorymanagementapp.exception.auth.TokenNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.confirmationToken.ConfirmationTokenExpiredException;
import pl.pomoku.inventorymanagementapp.exception.confirmationToken.ConfirmationTokenNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.role.RoleNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.token.InvalidTokenException;
import pl.pomoku.inventorymanagementapp.exception.user.UserAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.user.UserNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.RoleRepository;
import pl.pomoku.inventorymanagementapp.repository.TokenRepository;
import pl.pomoku.inventorymanagementapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final EmailSenderService emailSenderService;

    @Transactional
    public String register(RegisterUserDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistException(request.getEmail());
        }

        Role role = roleRepository.findByName("user")
                .orElseThrow(() -> new RoleNotFoundException(request.getEmail()));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(List.of(role)))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build();

        user = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .build();

        confirmationTokenService.save(confirmationToken);
        return token;
    }

    @Transactional
    public String registerAndSendMail(RegisterUserDTO request) {
        String confirmationToken = register(request);
        String link = "http://localhost:8080/api/v1/auth/confirm-account?token=" + confirmationToken;
        emailSenderService.send(
                request.getEmail(),
                "%s %s".formatted(request.getFirstName(), request.getLastName()),
                link
        );
        return confirmationToken;
    }

    public AuthResponse authenticate(AuthDTO request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        var auth = authenticationManager.authenticate(buildAuthToken(request));

        if (auth.isAuthenticated()) {
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            tokenRepository.save(new Token(jwtToken, user));
            return new AuthResponse(jwtToken, refreshToken);
        } else {
            //TODO: Check if user have activated account and if not throw custom exception
            throw new InvalidPasswordOrEmailException();
        }
    }

    public String confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(ConfirmationTokenNotFoundException::new);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new AccountAlreadyConfirmException();
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenExpiredException();
        }

        confirmationTokenService.confirmAt(token);
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "Account successfully confirmed";
    }

    public AuthResponse generateNewTokensByRefreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException();
        }

        String newToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        tokenRepository.save(new Token(newToken, user));
        return new AuthResponse(newToken, refreshToken);
    }

    public boolean isTokenValid(String token) {
        Token jwtToken = tokenRepository.findByToken(token)
                .orElseThrow(TokenNotFoundException::new);

        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return jwtService.isTokenValid(token, user)
                && !jwtToken.isExpired() && !jwtToken.isRevoked();
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(AuthDTO dto) {
        return new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
    }
}
