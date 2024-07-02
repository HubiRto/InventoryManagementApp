package pl.pomoku.inventorymanagementapp.dto.response;

public record AuthResponse(
        String token,
        String refreshToken
) {}
