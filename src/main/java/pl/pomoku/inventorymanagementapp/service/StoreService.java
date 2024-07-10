package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateStoreDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateStoreDTO;
import pl.pomoku.inventorymanagementapp.entity.Event;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.store.StoreAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.store.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.mapper.StoreMapper;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final EventRepository eventRepository;
    private final StoreMapper storeMapper;

    public Store getById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Transactional
    public Store createStore(CreateStoreDTO request, User user) {
        if (storeRepository.existsByName(request.getName())) {
            throw new StoreAlreadyExistException(request.getName());
        }

        Store store = storeMapper.mapToEntity(request);
        store.setUser(user);
        store = storeRepository.save(store);

        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create store with name (%s)"
                        .formatted(user.getFullName(), store.getName())
        );
        eventRepository.save(event);

        return store;
    }

    @Transactional
    public Store updateStore(UpdateStoreDTO request, User user, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        if (store.getName().equals(request.getNewStoreName())) {
            return store;
        }

        Event event = new Event(
                EventType.UPDATE,
                "Admin (%s) change store name from (%s) to (%s)"
                        .formatted(user.getFullName(), store.getName(), request.getNewStoreName())
        );

        store.setName(request.getNewStoreName());
        store = storeRepository.save(store);

        eventRepository.save(event);

        return store;
    }

    @Transactional
    public void deleteById(Long storeId, User user) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));
        storeRepository.delete(store);

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete store (%s)".formatted(user.getFullName(), store.getName())
        );
        eventRepository.save(event);
    }
}
