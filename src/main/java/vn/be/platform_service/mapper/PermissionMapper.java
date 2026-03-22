package vn.be.platform_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vn.be.platform_service.dto.PermissionDTO;
import vn.be.platform_service.entity.Permission;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    PermissionDTO toDTO(Permission permission);

    Permission toEntity(PermissionDTO dto);

    List<PermissionDTO> toDTOs(List<Permission> permissions);
}
