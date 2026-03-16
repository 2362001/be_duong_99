package vn.be.platform_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.be.platform_service.dto.ProductDTO;

public interface ProductService {
    Page<ProductDTO> getProducts(Pageable pageable);

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);
}
