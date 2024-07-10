package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillboardDTO {
    private Long id;
    private String name;
    private String label;
    private String type;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}