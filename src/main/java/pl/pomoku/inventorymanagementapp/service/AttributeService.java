package pl.pomoku.inventorymanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pomoku.inventorymanagementapp.dto.request.CreateAttributeDTO;
import pl.pomoku.inventorymanagementapp.dto.request.UpdateAttributeDTO;
import pl.pomoku.inventorymanagementapp.entity.Attribute;
import pl.pomoku.inventorymanagementapp.entity.Store;
import pl.pomoku.inventorymanagementapp.exception.StoreNotFoundException;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeAlreadyExistException;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeDeletionException;
import pl.pomoku.inventorymanagementapp.exception.attribute.AttributeNotFoundException;
import pl.pomoku.inventorymanagementapp.mapper.AttributeMapper;
import pl.pomoku.inventorymanagementapp.repository.AttributeRepository;
import pl.pomoku.inventorymanagementapp.repository.ProductAttributeRepository;
import pl.pomoku.inventorymanagementapp.repository.StoreRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttributeService {
    private final StoreRepository storeRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final ProductAttributeRepository productAttributeRepository;

    public Attribute addNewAttribute(CreateAttributeDTO request, UUID storeId) {
        if (attributeRepository.existsByName(request.getName())) {
            throw new AttributeAlreadyExistException();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        Attribute attribute = attributeMapper.mapToEntity(request);
        attribute.setStore(store);

        return attributeRepository.save(attribute);
    }

    public Attribute updateAttribute(UpdateAttributeDTO request, Long attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(AttributeNotFoundException::new);

        if (attribute.getName().equals(request.getName())) {
            attribute.setName(request.getName());
            return attributeRepository.save(attribute);
        }

        return attribute;
    }

    public Attribute getAttributeById(Long attributeId) {
        return attributeRepository.findById(attributeId)
                .orElseThrow(AttributeNotFoundException::new);
    }

    public List<Attribute> getAllAttributeByStoreId(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        return attributeRepository.findAllByStore(store);
    }


    public void deleteByAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(AttributeNotFoundException::new);

        if (productAttributeRepository.countByAttributeId(id) != 0) {
            throw new AttributeDeletionException();
        }

        attributeRepository.delete(attribute);
    }
}
