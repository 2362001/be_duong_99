package vn.be.platform_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.be.platform_service.dto.PermissionDTO;

public interface PermissionService {
    Page<PermissionDTO> getPermissions(Pageable pageable);

    PermissionDTO addPermission(PermissionDTO permissionDTO);

    void deletePermission(Long id);

    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    PermissionDTO getPermissionById(Long id);
}
