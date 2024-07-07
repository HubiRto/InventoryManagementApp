package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID billboardId;
    private String billboardLabel;
}
