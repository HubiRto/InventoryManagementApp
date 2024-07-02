package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.AuthRequest;
import pl.pomoku.inventorymanagementapp.dto.request.RegisterUserRequest;
import pl.pomoku.inventorymanagementapp.service.AuthService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) throws URISyntaxException {
        String confirmationToken = authService.registerAndSendMail(request);
        String link = "http://localhost:8080/api/v1/auth/confirm-account?token=" + confirmationToken;
        return ResponseEntity.created(new URI(link)).body(confirmationToken);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/confirm-account")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> confirmAccount(@Param("token") String token) {
        return ResponseEntity.ok(authService.confirmAccount(token));
    }
}
