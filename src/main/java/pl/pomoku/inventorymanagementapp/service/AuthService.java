package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.AuthRequest;
import pl.pomoku.inventorymanagementapp.dto.request.RegisterUserRequest;
import pl.pomoku.inventorymanagementapp.dto.response.AuthResponse;
import pl.pomoku.inventorymanagementapp.entity.ConfirmationToken;
import pl.pomoku.inventorymanagementapp.entity.Role;
import pl.pomoku.inventorymanagementapp.entity.Token;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.exception.*;
import pl.pomoku.inventorymanagementapp.repository.RoleRepository;
import pl.pomoku.inventorymanagementapp.repository.TokenRepository;
import pl.pomoku.inventorymanagementapp.repository.UserRepository;

import java.time.LocalDateTime;
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
    public String register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) throw new UserAlreadyExistException(request.email());

        //TODO: Create email and password validation

        String encodedPassword = passwordEncoder.encode(request.password());
        Role role = roleRepository.findByName("user");
        User user = new User(request.firstName(), request.lastName(), request.email(), encodedPassword, role);
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
    public String registerAndSendMail(RegisterUserRequest request) {
        String confirmationToken = register(request);
        String link = "http://localhost:8080/api/v1/auth/confirm-account?token=" + confirmationToken;
        emailSenderService.send(request.email(), request.getFullName(), link);
        return confirmationToken;
    }

    public AuthResponse authenticate(AuthRequest request) {
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));
        var auth = authenticationManager.authenticate(buildAuthToken(request));

        if (auth.isAuthenticated()) {
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            tokenRepository.save(new Token(jwtToken, user));
            return new AuthResponse(jwtToken, refreshToken);
        } else {
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

    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(AuthRequest loginRequest) {
        return new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
    }
}
