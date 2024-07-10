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
import pl.pomoku.inventorymanagementapp.dto.request.CreateStoreDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateStoreDTO;
import pl.pomoku.inventorymanagementapp.dto.response.StoreDTO;
import pl.pomoku.inventorymanagementapp.mapper.StoreMapper;
import pl.pomoku.inventorymanagementapp.service.StoreService;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Validated
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;
    private final StoreMapper storeMapper;

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> getStoreById(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @PathVariable("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                storeMapper.mapToDto(storeService.getById(storeId)),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return new ResponseEntity<>(
                storeService.getAllStores().stream().map(storeMapper::mapToDto).toList(),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<StoreDTO> createStore(
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateStoreDTO request
    ) {
        return new ResponseEntity<>(
                storeMapper.mapToDto(storeService.createStore(request, userService.getUserFromToken(token))),
                HttpStatus.CREATED
        );
    }

    @Secured("ADMIN")
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreDTO> updateStoreById(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @PathVariable(name = "storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateStoreDTO request
    ) {
        return new ResponseEntity<>(
                storeMapper.mapToDto(storeService.updateStore(request, userService.getUserFromToken(token), storeId)),
                HttpStatus.OK);
    }

    @Secured("ADMIN")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStoreById(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @PathVariable(name = "storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        storeService.deleteById(storeId, userService.getUserFromToken(token));
        return new ResponseEntity<>(
                "Store with ID " + storeId + " has been deleted.",
                HttpStatus.OK
        );
    }
}
