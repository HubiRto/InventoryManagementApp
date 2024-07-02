package pl.pomoku.inventorymanagementapp.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
