package pl.pomoku.inventorymanagementapp.controller.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.CreateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.response.AttributeDTO;
import pl.pomoku.inventorymanagementapp.mapper.AttributeMapper;
import pl.pomoku.inventorymanagementapp.service.AttributeService;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attribute")
@RequiredArgsConstructor
@Validated
public class AttributeController {
    private final AttributeService attributeService;
    private final AttributeMapper attributeMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributesByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                attributeService.getAllAttributeByStoreId(storeId).stream()
                        .map(attributeMapper::mapToDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> getAttributeById(
            @NotNull(message = "Attribute ID cannot be null")
            @Min(value = 1, message = "Attribute ID must be a non-negative number")
            @PathVariable("attributeId") Long attributeId
    ) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.getAttributeById(attributeId)),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateAttributeDTO request
    ) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.addNewAttribute(
                        request,
                        userService.getUserFromToken(token),
                        storeId
                )),
                HttpStatus.CREATED
        );
    }

    @Secured("ADMIN")
    @PatchMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> createProduct(
            @NotNull(message = "Attribute ID cannot be null")
            @Min(value = 1, message = "Attribute ID must be a non-negative number")
            @PathVariable("attributeId") Long attributeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateAttributeDTO request
    ) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.updateAttribute(
                        request,
                        userService.getUserFromToken(token),
                        attributeId
                )),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @DeleteMapping("/{attributeId}")
    public ResponseEntity<String> deleteProduct(
            @NotNull(message = "Attribute ID cannot be null")
            @Min(value = 1, message = "Attribute ID must be a non-negative number")
            @PathVariable("attributeId") Long attributeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        attributeService.deleteByAttributeById(attributeId, userService.getUserFromToken(token));
        return ResponseEntity.ok("Successfully deleted attribute");
    }
}