package pl.pomoku.inventorymanagementapp.dto.request;

import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;

import java.time.LocalDateTime;

public record CreateStoreRequest(
        String name,
        Long userId
) {
    public Store mapToEntity(User user) {
        return Store.builder()
                .name(name)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
