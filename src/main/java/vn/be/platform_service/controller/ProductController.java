package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.ProductDTO;
import vn.be.platform_service.service.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductDTO>> getProducts(Pageable pageable) {
        return ApiResponse.success(productService.getProducts(pageable));
    }

    @PostMapping
    public ApiResponse<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        return ApiResponse.success(productService.addProduct(productDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
       return  ApiResponse.success(productService.updateProduct(id, productDTO));
    }

    // thêm api xóa mềm ( đồng nghĩa với việc e thêm 1 cột trong bảng product ) . mặc định thì em set các giá trị của cột đó là 1 ( active ) và em cần lấy ra tất cả sản phẩm có trạng thái active =1 (đối với api get ). khi mà xóa mềm thì em phải cập nhật lại giá trị cột đó là 0 ( inactive )

    // viết api get by id

}
