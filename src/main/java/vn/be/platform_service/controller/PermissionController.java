package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.constant.ApiConstant;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.PermissionDTO;
import vn.be.platform_service.service.PermissionService;

@RestController
@RequestMapping(ApiConstant.API_PREFIX + "/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public ApiResponse<Page<PermissionDTO>> getPermissions(Pageable pageable){
        return ApiResponse.success(permissionService.getPermissions(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<PermissionDTO> getPermissionById(@PathVariable Long id){
        return ApiResponse.success(permissionService.getPermissionById(id));
    }

    @PostMapping("/{id}")
    public ApiResponse<PermissionDTO> addPermission(@RequestBody PermissionDTO permissionDTO){
        return ApiResponse.success((permissionService.addPermission(permissionDTO)));
    }

    @PutMapping("/{id}")
    public ApiResponse<PermissionDTO> updatePermission(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO){
        return ApiResponse.success(permissionService.updatePermission(id, permissionDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
        return ApiResponse.success(null);
    }
}
