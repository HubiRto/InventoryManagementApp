package pl.pomoku.inventorymanagementapp.controller.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.AuthDTO;
import pl.pomoku.inventorymanagementapp.dto.request.RegisterUserDTO;
import pl.pomoku.inventorymanagementapp.dto.response.AuthResponse;
import pl.pomoku.inventorymanagementapp.service.AuthService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO request) throws URISyntaxException {
        String confirmationToken = authService.registerAndSendMail(request);
        String link = "http://localhost:8080/api/v1/auth/confirm-account?token=" + confirmationToken;
        return ResponseEntity.created(new URI(link)).body(confirmationToken);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @Param("token") String token
    ) {
        return ResponseEntity.ok(authService.confirmAccount(token));
    }


}
