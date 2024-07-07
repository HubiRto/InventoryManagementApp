package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.exception.BillboardNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.BillboardRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillboardService {
    private final BillboardRepository billboardRepository;
    private final StoreRepository storeRepository;

    public void deleteBillboardById(UUID billboardId) {
        Billboard billboard = billboardRepository.findById(billboardId).orElseThrow(() -> new BillboardNotFoundException(billboardId));
        billboardRepository.delete(billboard);
    }

    public Billboard updateBillboard(UUID billboardId, String label, MultipartFile file) throws IOException {
        Billboard billboard = billboardRepository.findById(billboardId).orElseThrow(() -> new BillboardNotFoundException(billboardId));
        billboard.setLabel(label);
        billboard.setUpdatedAt(LocalDateTime.now());
        if(file != null){
            billboard.setType(file.getContentType());
            billboard.setName(file.getOriginalFilename());
            billboard.setImage(ImageUtils.compressImage(file.getBytes()));
        }
        return billboardRepository.save(billboard);
    }

    @Transactional
    public Billboard addBillboard(UUID storeId, String label, MultipartFile file) throws IOException {
        if(file == null || file.getSize() == 0 || file.getContentType() == null){
            throw new AppException("File or Content Type is empty", HttpStatus.BAD_REQUEST);
        }
        
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Billboard billboard = Billboard.builder()
                .store(store)
                .type(file.getContentType())
                .name(file.getOriginalFilename())
                .label(label)
                .image(ImageUtils.compressImage(file.getBytes()))
                .createdAt(LocalDateTime.now())
                .build();

        billboard = billboardRepository.save(billboard);
        billboard.setImageUrl("http://localhost:8080/api/v1/billboards/" + billboard.getId() + "/image");
        return billboardRepository.save(billboard);
    }

    @Transactional
    public List<Billboard> getAllBillboards(UUID storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        return billboardRepository.findAllByStoreId(storeId);
    }

    public Billboard getBillboardById(UUID id) {
        return billboardRepository.findById(id).orElseThrow(() -> new BillboardNotFoundException(id));
    }
}
