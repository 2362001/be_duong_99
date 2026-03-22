package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.RoleDTO;
import vn.be.platform_service.entity.Role;
import vn.be.platform_service.mapper.RoleMapper;
import vn.be.platform_service.repositories.RoleRepository;
import vn.be.platform_service.service.RoleService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Page<RoleDTO> getRoles(Pageable pageable) {
        return roleRepository.findAll(pageable).map(roleMapper::toDTO);
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return roleMapper.toDTO(existingRole);
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        Instant now = Instant.now();
        Role role = roleMapper.toEntity(roleDTO);
        role.setCreatedAt(now);
        role.setUpdatedAt(now);
        Role saveRole = roleRepository.save(role);
        return roleMapper.toDTO(saveRole);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Instant now = Instant.now();
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(roleDTO.getName() != null){
            existingRole.setName(roleDTO.getName());
        }
        if(roleDTO.getDescription() != null){
            existingRole.setDescription(roleDTO.getDescription());
        }

        existingRole.setCreatedAt(now);
        existingRole.setUpdatedAt(now);

        Role newRole = roleRepository.save(existingRole);
        return roleMapper.toDTO(newRole);
    }
}
