package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.RoleDTO;
import vn.be.platform_service.service.RoleService;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ApiResponse<Page<RoleDTO>> getRoles(Pageable pageable){
        return ApiResponse.success(roleService.getRoles(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Long id){
        return ApiResponse.success(roleService.getRoleById(id));
    }

    @PostMapping("{/id}")
    public ApiResponse<RoleDTO> addRole(@RequestBody RoleDTO roleDTO){
        return ApiResponse.success((roleService.addRole(roleDTO)));
    }

    @PutMapping("{/id}")
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO){
        return ApiResponse.success(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("{/id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }
}
