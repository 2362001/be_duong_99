package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.constant.ApiConstant;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.MenuDTO;
import vn.be.platform_service.service.MenuService;

@RestController
@RequestMapping(ApiConstant.API_PREFIX + "/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ApiResponse<Page<MenuDTO>> getMenus(Pageable pageable){
        return ApiResponse.success(menuService.getMenus(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<MenuDTO> getMenuById(@PathVariable Long id){
        return ApiResponse.success(menuService.getMenuById(id));
    }

    @PostMapping("/{id}")
    public ApiResponse<MenuDTO> addMenu(@RequestBody MenuDTO menuDTO){
        return ApiResponse.success((menuService.addMenu(menuDTO)));
    }

    @PutMapping("/{id}")
    public ApiResponse<MenuDTO> updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO){
        return ApiResponse.success(menuService.updateMenu(id, menuDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long id){
        menuService.deleteMenu(id);
        return ApiResponse.success(null);
    }
}
