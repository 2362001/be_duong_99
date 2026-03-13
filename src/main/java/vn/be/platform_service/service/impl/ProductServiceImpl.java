package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.be.platform_service.dto.ProductDTO;
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
        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

}
