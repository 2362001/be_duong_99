package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.ProductDTO;
import vn.be.platform_service.entity.Product;
import vn.be.platform_service.mapper.ProductMapper;
import vn.be.platform_service.repositories.ProductRepository;
import vn.be.platform_service.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDTO> getProducts(Pageable pageable) {
//        return productRepository.findAll(pageable).map(productMapper::toDTO);
        return productRepository.getAllProductActive(1, pageable).map(productMapper::toDTO);
    }


    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }

        Product newProduct = productRepository.save(existingProduct);
        ProductDTO updatedProductDTO = productMapper.toDTO(newProduct);

        return updatedProductDTO;
    }
}
