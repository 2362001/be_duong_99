package vn.be.platform_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vn.be.platform_service.dto.RoleDTO;
import vn.be.platform_service.entity.Role;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO dto);

    List<RoleDTO> toDTOs(List<Role> role);
}
