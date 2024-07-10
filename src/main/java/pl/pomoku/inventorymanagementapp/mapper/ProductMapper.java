package pl.pomoku.inventorymanagementapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import pl.pomoku.inventorymanagementapp.dto.response.ProductDetailsDTO;
import pl.pomoku.inventorymanagementapp.dto.response.ProductSummaryDTO;
import pl.pomoku.inventorymanagementapp.entity.Product;
import pl.pomoku.inventorymanagementapp.entity.ProductAttribute;
import pl.pomoku.inventorymanagementapp.repository.AttributeRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public abstract class ProductMapper {
    @Autowired
    protected AttributeRepository attributeRepository;


    @Mappings({
            @Mapping(source = "producent.id", target = "producentId"),
            @Mapping(source = "producent.name", target = "producentName"),
            @Mapping(source = "stock.quantity", target = "availableInStock", qualifiedByName = "quantityToAvailableInStock"),
            @Mapping(source = "price.netPrice", target = "netPrice"),
            @Mapping(source = "price.grossPrice", target = "grossPrice")
    })
    public abstract ProductSummaryDTO mapToSummaryDTO(Product product);

    @Mappings({
            @Mapping(source = "producent.id", target = "producentId"),
            @Mapping(source = "producent.name", target = "producentName"),
            @Mapping(source = "stock.quantity", target = "availableInStock", qualifiedByName = "quantityToAvailableInStock"),
            @Mapping(source = "price.netPrice", target = "netPrice"),
            @Mapping(source = "price.grossPrice", target = "grossPrice"),
            @Mapping(source = "price.vat", target = "vat"),
            @Mapping(source = "productAttributes", target = "attributes", qualifiedByName = "mapAttributes")
    })
    public abstract ProductDetailsDTO mapToProductDetailsDTO(Product product);

    @Named("quantityToAvailableInStock")
    protected boolean quantityToAvailableInStock(int quantity) {
        return quantity > 0;
    }

    @Named("mapAttributes")
    protected Map<String, String> mapAttributes(List<ProductAttribute> productAttributes) {
        return productAttributes.stream()
                .collect(Collectors.toMap(
                        attribute -> attribute.getAttribute().getName(),
                        ProductAttribute::getValue
                ));
    }
}
