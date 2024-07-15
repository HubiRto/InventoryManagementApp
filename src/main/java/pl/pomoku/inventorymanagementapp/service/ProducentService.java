package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateProducentDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProducentNameDTO;
import pl.pomoku.inventorymanagementapp.entity.Event;
import pl.pomoku.inventorymanagementapp.entity.Producent;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.producent.ProducentAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.producent.ProducentNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.store.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.mapper.ProducentMapper;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.ProducentRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducentService {
    private final StoreRepository storeRepository;
    private final ProducentRepository producentRepository;
    private final ProducentMapper producentMapper;
    private final EventRepository eventRepository;

    @Transactional
    public Producent addNewProducent(CreateProducentDTO request, User user, Long storeId) {
        if (producentRepository.existsByName(request.getName())) {
            throw new ProducentAlreadyExistException(request.getName());
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Producent producent = producentMapper.mapToEntity(request);
        producent.setStore(store);

        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create producent with name (%s)"
                        .formatted(user.getFullName(), producent.getName())
        );

        eventRepository.save(event);
        return producentRepository.save(producent);
    }

    @Transactional
    public Producent updateProducent(UpdateProducentDTO request, User user, Long producentId) {
        Producent producent = producentRepository.findById(producentId)
                .orElseThrow(() -> new ProducentNotFoundException(producentId));

        producent.setName(request.getName());
        producent.setDescription(request.getDescription());
        producent.setWebsite(request.getWebsite());

        Event event = new Event(
                EventType.UPDATE,
                "Admin (%s) updated producent with name (%s)"
                        .formatted(user.getFullName(), producent.getName())
        );
        eventRepository.save(event);

        return producentRepository.save(producent);
    }

    public Producent getProducentById(Long producentId) {
        return producentRepository.findById(producentId)
                .orElseThrow(() -> new ProducentNotFoundException(producentId));
    }

    public List<Producent> getAllProducentsByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        return producentRepository.findAllByStore(store);
    }

    @Transactional
    public void deleteProducentById(Long producentId, User user) {
        Producent producent = producentRepository.findById(producentId)
                .orElseThrow(() -> new ProducentNotFoundException(producentId));

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete producent (%s)".formatted(user.getFullName(), producent.getName())
        );
        eventRepository.save(event);

        producentRepository.delete(producent);
    }
}
