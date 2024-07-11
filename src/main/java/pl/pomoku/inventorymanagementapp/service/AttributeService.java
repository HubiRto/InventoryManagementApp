package pl.pomoku.inventorymanagementapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateAttributeDTO;
import pl.pomoku.inventorymanagementapp.entity.Attribute;
import pl.pomoku.inventorymanagementapp.entity.Event;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.entity.User;
import pl.pomoku.inventorymanagementapp.enumerated.EventType;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeDeletionException;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.store.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.mapper.AttributeMapper;
import pl.pomoku.inventorymanagementapp.repository.AttributeRepository;
import pl.pomoku.inventorymanagementapp.repository.EventRepository;
import pl.pomoku.inventorymanagementapp.repository.ProductAttributeRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final StoreRepository storeRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final ProductAttributeRepository productAttributeRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Attribute addNewAttribute(CreateAttributeDTO request, User user, Long storeId) {
        if (attributeRepository.existsByName(request.getName())) {
            throw new AttributeAlreadyExistException();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        Attribute attribute = attributeMapper.mapToEntity(request);
        attribute.setStore(store);
        attribute.setCreatedBy(user);

        Event event = new Event(
                EventType.CREATE,
                "Admin (%s) create attribute with name (%s)"
                        .formatted(user.getFullName(), attribute.getName())
        );

        eventRepository.save(event);
        return attributeRepository.save(attribute);
    }

    @Transactional
    public Attribute updateAttribute(UpdateAttributeDTO request, User user, Long attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(AttributeNotFoundException::new);

        if (attribute.getName().equals(request.getName())) {
            Event event = new Event(
                    EventType.CREATE,
                    "Admin (%s) change attribute name to (%s)"
                            .formatted(user.getFullName(), attribute.getName())
            );
            eventRepository.save(event);

            attribute.setName(request.getName());
            return attributeRepository.save(attribute);
        }

        return attribute;
    }

    public Attribute getAttributeById(Long attributeId) {
        return attributeRepository.findById(attributeId)
                .orElseThrow(AttributeNotFoundException::new);
    }

    public List<Attribute> getAllAttributeByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(storeId));

        return attributeRepository.findAllByStore(store);
    }


    @Transactional
    public void deleteByAttributeById(Long attributeId, User user) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(AttributeNotFoundException::new);

        if (productAttributeRepository.countByAttributeId(attributeId) != 0) {
            throw new AttributeDeletionException();
        }

        Event event = new Event(
                EventType.DELETE,
                "Admin (%s) delete attribute (%s)".formatted(user.getFullName(), attribute.getName())
        );
        eventRepository.save(event);

        attributeRepository.delete(attribute);
    }
}
