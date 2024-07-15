package pl.pomoku.inventorymanagementapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProducentDTO {
    private Long id;
    private String name;
    private String website;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}