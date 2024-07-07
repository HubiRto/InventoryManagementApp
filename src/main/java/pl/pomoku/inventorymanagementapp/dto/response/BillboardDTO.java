package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillboardDTO {
    private UUID id;
    private String name;
    private String label;
    private String type;
    private String imageUrl;
    private UUID storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}