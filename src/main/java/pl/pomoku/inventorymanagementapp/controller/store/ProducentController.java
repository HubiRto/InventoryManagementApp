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
import pl.pomoku.inventorymanagementapp.dto.request.CreateProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProducentNameDTO;
import pl.pomoku.inventorymanagementapp.mapper.ProducentMapper;
import pl.pomoku.inventorymanagementapp.service.ProducentService;
import pl.pomoku.inventorymanagementapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producents")
@RequiredArgsConstructor
@Validated
public class ProducentController {
    private final ProducentService producentService;
    private final ProducentMapper producentMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProducentDTO>> getAllProducentsByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                producentService.getAllProducentsByStoreId(storeId).stream()
                        .map(producentMapper::mapToDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{producentId}")
    public ResponseEntity<ProducentDTO> getProducentById(
            @NotNull(message = "Producent ID cannot be null")
            @Min(value = 1, message = "Producent ID must be a non-negative number")
            @PathVariable("producentId") Long producentId
    ) {
        return new ResponseEntity<>(
                producentMapper.mapToDTO(producentService.getProducentById(producentId)),
                HttpStatus.OK
        );
    }

    @GetMapping("/names")
    public ResponseEntity<List<ProducentNameDTO>> getProducentNamesByStoreId(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId
    ) {
        return new ResponseEntity<>(
                producentService.getAllProducentsByStoreId(storeId).stream()
                        .map(producentMapper::mapToNameDTO)
                        .toList(),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @PostMapping
    public ResponseEntity<ProducentDTO> createProducent(
            @NotNull(message = "Store ID cannot be null")
            @Min(value = 1, message = "Store ID must be a non-negative number")
            @RequestParam("storeId") Long storeId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateProducentDTO request
    ) {
        return new ResponseEntity<>(
                producentMapper.mapToDTO(producentService.addNewProducent(
                        request,
                        userService.getUserFromToken(token),
                        storeId
                )),
                HttpStatus.CREATED
        );
    }

    @Secured("ADMIN")
    @PatchMapping("/{producentId}")
    public ResponseEntity<ProducentDTO> updateProducent(
            @NotNull(message = "Producent ID cannot be null")
            @Min(value = 1, message = "Producent ID must be a non-negative number")
            @PathVariable("producentId") Long producentId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UpdateProducentDTO request
    ) {
        return new ResponseEntity<>(
                producentMapper.mapToDTO(producentService.updateProducent(
                        request,
                        userService.getUserFromToken(token),
                        producentId
                )),
                HttpStatus.OK
        );
    }

    @Secured("ADMIN")
    @DeleteMapping("/{producentId}")
    public ResponseEntity<String> deleteProducent(
            @NotNull(message = "Producent ID cannot be null")
            @Min(value = 1, message = "Producent ID must be a non-negative number")
            @PathVariable("producentId") Long producentId,
            @NotNull(message = "Token cannot be null")
            @NotEmpty(message = "Token cannot be empty")
            @RequestHeader("Authorization") String token
    ) {
        producentService.deleteProducentById(producentId, userService.getUserFromToken(token));
        return ResponseEntity.ok("Successfully deleted producent");
    }
}