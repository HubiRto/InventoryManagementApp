package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pomoku.inventorymanagementapp.entity.Billboard;
import pl.pomoku.inventorymanagementapp.entity.Event;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.AppException;
import pl.pomoku.inventorymanagementapp.exception.billboard.BillboardAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.billboard.BillboardNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.store.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.repository.BillboardRepository;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;
import pl.pomoku.inventorymanagementapp.utils.ImageUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillboardService {
    private final BillboardRepository billboardRepository;
    private final StoreRepository storeRepository;
    private final EventRepository eventRepository;

    public List<Billboard> getAllBillboards(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new StoreNotFoundException(storeId);
        }

        return billboardRepository.findAllByStoreId(storeId);
    }

    public Billboard getBillboardById(Long billboardId) {
        return billboardRepository.findById(billboardId)
                .orElseThrow(() -> new BillboardNotFoundException(billboardId));
    }

    @Transactional
    public void deleteBillboardById(Long billboardId, User user) {
        Billboard billboard = billboardRepository.findById(billboardId)
                .orElseThrow(() -> new BillboardNotFoundException(billboardId));

        billboardRepository.delete(billboard);

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete billboard (%s)".formatted(user.getFullName(), billboard.getLabel())
        );
        eventRepository.save(event);
    }

    @Transactional
    public Billboard updateBillboard(Long billboardId, User user, String label, MultipartFile file) throws IOException {
        Billboard billboard = billboardRepository.findById(billboardId)
                .orElseThrow(() -> new BillboardNotFoundException(billboardId));

        List<Event> events = new ArrayList<>();

        // If statement order is important, because if label change
        // event data will be incorrect
        if (file != null) {
            byte[] bytes = ImageUtils.compressImage(file.getBytes());
            if (!Arrays.equals(bytes, billboard.getImage())) {
                events.add(new Event(
                        EventType.UPDATE,
                        "Admin (%s) change billboard image in billboard (%s)"
                                .formatted(user.getFullName(), billboard.getLabel())
                ));
                billboard.setImage(bytes);
                billboard.setType(file.getContentType());
                billboard.setName(file.getOriginalFilename());
            }
        }

        if (label != null && billboard.getLabel().equals(label)) {
            events.add(new Event(
                    EventType.UPDATE,
                    "Admin (%s) change billboard label from (%s) to (%s)"
                            .formatted(user.getFullName(), billboard.getLabel(), label)
            ));
            billboard.setLabel(billboard.getLabel());
        }

        if (!events.isEmpty()) {
            eventRepository.saveAll(events);
        }

        return billboardRepository.save(billboard);
    }

    @Transactional
    public Billboard addBillboard(Long storeId, User user, String label, MultipartFile file) throws IOException {
        if (billboardRepository.existsByLabel(label)) {
            throw new BillboardAlreadyExistException(label);
        }

        if (file == null || file.getSize() == 0 || file.getContentType() == null) {
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

        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create billboard with label (%s)"
                        .formatted(user.getFullName(), billboard.getLabel())
        );
        eventRepository.save(event);

        billboard.setImageUrl("http://localhost:8080/api/v1/billboards/" + billboard.getId() + "/image");
        return billboardRepository.save(billboard);
    }
}
