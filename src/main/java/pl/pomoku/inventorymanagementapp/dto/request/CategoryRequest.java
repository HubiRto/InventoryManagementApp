package pl.pomoku.inventorymanagementapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryRequest {
    private String name;
    private UUID billboardId;
}
