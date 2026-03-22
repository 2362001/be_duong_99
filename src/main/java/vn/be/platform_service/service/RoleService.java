package vn.be.platform_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.be.platform_service.dto.RoleDTO;

public interface RoleService {
    Page<RoleDTO> getRoles(Pageable pageable);

    RoleDTO addRole(RoleDTO roleDTO);

    void deleteRole(Long id);

    RoleDTO updateRole(Long id, RoleDTO roleDTO);

    RoleDTO getRoleById(Long id);
}
