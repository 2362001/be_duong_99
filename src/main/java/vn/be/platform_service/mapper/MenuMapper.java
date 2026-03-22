package vn.be.platform_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vn.be.platform_service.dto.MenuDTO;
import vn.be.platform_service.entity.Menu;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuMapper {
    MenuDTO toDTO(Menu menu);

    Menu toEntity(MenuDTO dto);

    List<MenuDTO> toDTOs(List<Menu> menus);
}
