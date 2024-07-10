
package pl.pomoku.inventorymanagementapp.controller.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardDTO;
import pl.pomoku.inventorymanagementapp.dto.response.BillboardNameDTO;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.mapper.BillboardMapper;
import pl.pomoku.inventorymanagementapp.service.BillboardService;
import pl.pomoku.inventorymanagementapp.service.UserService;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/billboards")
@RequiredArgsConstructor
@Validated
public class BillboardController {
    private final BillboardService billboardService;
    private final BillboardMapper billboardMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BillboardDTO>> getAllBillboardsByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                billboardService.getAllBillboards(storeId).stream()
                        .map(billboardMapper::mapToBillboardDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/names")
    public ResponseEntity<List<BillboardNameDTO>> getAllBillboardsNamesByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                billboardService.getAllBillboards(storeId).stream()
                        .map(billboardMapper::mapToBillboardNameDTO)
                        .toList(),
                HttpStatus.OK
        );
    }


    @GetMapping("/{billboardId}")
    public ResponseEntity<BillboardDTO> getBillboardById(
            @NotNull(message = "Billboard ID cannot be null")
            @Min(value = 1, message = "Billboard ID must be a non-negative number")
            @PathVariable Long billboardId
    ) {
        return new ResponseEntity<>(
                billboardMapper.mapToBillboardDTO(billboardService.getBillboardById(billboardId)),
                HttpStatus.OK
        );
    }

    @GetMapping("/{billboardId}/image")
    public ResponseEntity<byte[]> getBillboardImageById(@PathVariable Long billboardId) {
        Billboard billboard = billboardService.getBillboardById(billboardId);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(billboard.getType()))
                .body(ImageUtils.decompressImage(billboard.getImage()));
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<BillboardDTO> createBillboard(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId,
            @NotNull(message = "Label cannot be null")
            @NotEmpty(message = "Label cannot be empty")
            @Size(min = 5, message = "Label must have at least 5 characters")
            @RequestParam("label") String label,
            @NotNull(message = "Image cannot be null")
            @RequestParam("image") MultipartFile imageFile,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        try {
            return new ResponseEntity<>(
                    billboardMapper.mapToBillboardDTO(billboardService.addBillboard(
                            storeId,
                            userService.getUserFromToken(token),
                            label,
                            imageFile
                    )),
                    HttpStatus.CREATED
            );
        } catch (IOException e) {
            throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ADMIN")
    @PatchMapping("/{billboardId}")
    public ResponseEntity<BillboardDTO> updateBillboard(
            @NotNull(message = "Billboard ID cannot be null")
            @Min(value = 1, message = "Billboard ID must be a non-negative number")
            @PathVariable("billboardId") Long billboardId,
            @RequestParam(value = "label", required = false) String label,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        try {
            return new ResponseEntity<>(
                    billboardMapper.mapToBillboardDTO(billboardService.updateBillboard(
                            billboardId,
                            userService.getUserFromToken(token),
                            label,
                            imageFile
                    )),
                    HttpStatus.OK
            );
        } catch (IOException e) {
            throw new AppException("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ADMIN")
    @DeleteMapping("/{billboardId}")
    public ResponseEntity<?> updateStoreNameByStoreId(
            @NotNull(message = "Billboard ID cannot be null")
            @Min(value = 1, message = "Billboard ID must be a non-negative number")
            @PathVariable(name = "billboardId") Long billboardId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        billboardService.deleteBillboardById(billboardId, userService.getUserFromToken(token));
        return new ResponseEntity<>("Store successfully deleted", HttpStatus.OK);
    }
}
