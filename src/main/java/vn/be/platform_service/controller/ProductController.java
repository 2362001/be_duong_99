package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.ProductDTO;
import vn.be.platform_service.entity.Product;
import vn.be.platform_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductDTO>> getProducts(Pageable pageable) {
        return ApiResponse.success(productService.getProducts(pageable));
    }
}
