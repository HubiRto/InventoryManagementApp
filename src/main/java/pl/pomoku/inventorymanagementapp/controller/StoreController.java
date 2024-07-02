package pl.pomoku.inventorymanagementapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pomoku.inventorymanagementapp.dto.request.CreateStoreRequest;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.service.StoreService;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/create")
    public ResponseEntity<?> createStore(@RequestBody CreateStoreRequest request) {
        return ResponseEntity.ok().body(storeService.createStore(request).toDto());
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreById(@PathVariable UUID storeId) {
        return new ResponseEntity<>(storeService.getById(storeId).toDto(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllStores(@RequestParam(name = "userId") Long userId) {
        return new ResponseEntity<>(storeService.getAllStoresByUserId(userId).stream()
                .map(Store::toDto).collect(Collectors.toSet()), HttpStatus.OK);
    }

//    @PutMapping("/{storeId}")
//    public ResponseEntity<?> updateStoreNameByStoreId(@PathVariable(name = "storeId") UUID storeId) {
//        return new ResponseEntity<>(storeService.getAllStoresByUserId(userId).stream()
//                .map(Store::toDto).collect(Collectors.toSet()), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{storeId}")
//    public ResponseEntity<?> updateStoreNameByStoreId(@PathVariable(name = "storeId") UUID storeId) {
//        return new ResponseEntity<>(storeService.getAllStoresByUserId(userId).stream()
//                .map(Store::toDto).collect(Collectors.toSet()), HttpStatus.OK);
//    }
}
