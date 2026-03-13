package vn.be.platform_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vn.be.platform_service.dto.ProductDTO;
import vn.be.platform_service.entity.Product;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO dto);

    List<ProductDTO> toDTOs(List<Product> products);
}
