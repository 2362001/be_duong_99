package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.PermissionDTO;
import vn.be.platform_service.entity.Menu;
import vn.be.platform_service.entity.Permission;
import vn.be.platform_service.mapper.PermissionMapper;
import vn.be.platform_service.repositories.PermissionRepository;
import vn.be.platform_service.service.PermissionService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public Page<PermissionDTO> getPermissions(Pageable pageable) {
        return permissionRepository.findAll(pageable).map(permissionMapper::toDTO);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission existingPermission = permissionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return permissionMapper.toDTO(existingPermission);
    }

    @Override
    public PermissionDTO addPermission(PermissionDTO permissionDTO) {
        Instant now = Instant.now();
        Permission permission = permissionMapper.toEntity(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDTO(savedPermission);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Instant now = Instant.now();
        Permission existingPermission = permissionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(permissionDTO.getName() != null){
            existingPermission.setName(permissionDTO.getName());
        }
        if(permissionDTO.getDescription() != null){
            existingPermission.setDescription(permissionDTO.getDescription());
        }

        Permission newPermission = permissionRepository.save(existingPermission);
        return permissionMapper.toDTO(newPermission);
    }
}
