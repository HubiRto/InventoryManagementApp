
package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardDTO;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardNameDTO;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.mapper.BillboardMapper;
import pl.pomoku.inventorymanagementapp.service.BillboardService;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billboards")
@RequiredArgsConstructor
public class BillboardController {
    private final BillboardService billboardService;
    private final BillboardMapper billboardMapper;

    @GetMapping
    public ResponseEntity<List<BillboardDTO>> getAllBillboardsByStoreId(@RequestParam("storeId") UUID storeId) {
        return ResponseEntity.ok(
                billboardService.getAllBillboards(storeId).stream()
                        .map(billboardMapper::mapToBillboardDTO)
                        .toList()
        );
    }

    @GetMapping("/names")
    public ResponseEntity<List<BillboardNameDTO>> getAllBillboardsNamesByStoreId(@RequestParam("storeId") UUID storeId) {
        return ResponseEntity.ok(
                billboardService.getAllBillboards(storeId).stream()
                        .map(billboardMapper::mapToBillboardNameDTO)
                        .toList()
        );
    }


    @GetMapping("/{billboardId}")
    public ResponseEntity<BillboardDTO> getBillboardById(@PathVariable UUID billboardId) {
        return ResponseEntity.ok(
                billboardMapper.mapToBillboardDTO(billboardService.getBillboardById(billboardId))
        );
    }

    @PostMapping
    public ResponseEntity<BillboardDTO> createBillboard(
            @RequestParam("storeId") UUID storeId,
            @RequestParam("label") String label,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            return ResponseEntity.ok(
                    billboardMapper.mapToBillboardDTO(billboardService.addBillboard(storeId, label, imageFile))
            );
        } catch (IOException e) {
            throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{billboardId}")
    public ResponseEntity<BillboardDTO> updateBillboard(
            @PathVariable("billboardId") UUID billboardId,
            @RequestParam(value = "label", required = false) String label,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            return ResponseEntity.ok(
                    billboardMapper.mapToBillboardDTO(billboardService.updateBillboard(billboardId, label, imageFile))
            );
        } catch (IOException e) {
            throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{billboardId}")
    public ResponseEntity<?> updateStoreNameByStoreId(@PathVariable(name = "billboardId") UUID billboardId) {
        billboardService.deleteBillboardById(billboardId);
        return new ResponseEntity<>("Store successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/{billboardId}/image")
    public ResponseEntity<byte[]> getBillboardImageById(@PathVariable UUID billboardId) {
        Billboard billboard = billboardService.getBillboardById(billboardId);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(billboard.getType()))
                .body(ImageUtils.decompressImage(billboard.getImage()));
    }
}
