package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.AddProductDTO;
import pl.pomoku.inventorymanagementapp.dto.request.CreateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProductDTO;
import pl.pomoku.inventorymanagementapp.dto.response.AttributeDTO;
import pl.pomoku.inventorymanagementapp.mapper.AttributeMapper;
import pl.pomoku.inventorymanagementapp.service.AttributeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attribute")
@RequiredArgsConstructor
public class AttributeController {
    private final AttributeService attributeService;
    private final AttributeMapper attributeMapper;

    @GetMapping
    public ResponseEntity<List<AttributeDTO>> getAllAttributesByStoreId(@RequestParam("storeId") UUID storeId) {
        return new ResponseEntity<>(
                attributeService.getAllAttributeByStoreId(storeId).stream()
                        .map(attributeMapper::mapToDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> getAttributeById(@PathVariable("attributeId") Long attributeId) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.getAttributeById(attributeId)),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<AttributeDTO> createAttribute(
            @RequestBody CreateAttributeDTO request,
            @RequestParam("storeId") UUID storeId
    ) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.addNewAttribute(request, storeId)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{attributeId}")
    public ResponseEntity<AttributeDTO> createProduct(
            @RequestBody UpdateAttributeDTO request,
            @PathVariable("attributeId") Long attributeId
    ) {
        return new ResponseEntity<>(
                attributeMapper.mapToDTO(attributeService.updateAttribute(request, attributeId)),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{attributeId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("attributeId") Long attributeId) {
        attributeService.deleteByAttributeById(attributeId);
        return ResponseEntity.ok("Successfully deleted attribute");
    }
}