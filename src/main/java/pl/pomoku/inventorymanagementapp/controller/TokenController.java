package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.pomoku.inventorymanagementapp.dto.response.AuthResponse;
import pl.pomoku.inventorymanagementapp.entity.Token;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.exception.TokenNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.TokenRepository;
import pl.pomoku.inventorymanagementapp.repository.UserRepository;
import pl.pomoku.inventorymanagementapp.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Param("token") String token) {
        User user = userRepository.findByEmail(jwtService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!jwtService.isTokenValid(token, user)) throw new AppException(
                "Token jest niepoprawny",
                HttpStatus.BAD_REQUEST
        );

        String newToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        tokenRepository.save(new Token(newToken, user));
        return ResponseEntity.ok(new AuthResponse(newToken, token));
    }
    @PostMapping("/validate-token")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> validateToken(@Param("token") String token) {
        Token jwtToken = tokenRepository.findByToken(token)
                .orElseThrow(TokenNotFoundException::new);

        User user = userRepository.findByEmail(jwtService.extractUsername(token))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.accepted().body(
                jwtService.isTokenValid(token, user)
                        && !jwtToken.isExpired()
                        && !jwtToken.isRevoked()
        );
    }

    public void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
