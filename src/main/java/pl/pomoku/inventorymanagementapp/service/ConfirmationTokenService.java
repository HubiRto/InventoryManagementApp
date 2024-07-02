package pl.pomoku.inventorymanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.entity.ConfirmationToken;
import pl.pomoku.inventorymanagementapp.repository.ConfirmationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository repository;

    public void save(ConfirmationToken confirmationToken) {
        repository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return repository.findByToken(token);
    }

    public void confirmAt(String token) {
        repository.updateConfirmationToken(LocalDateTime.now(), token);
    }
}
