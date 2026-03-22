package vn.be.platform_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.be.platform_service.dto.MenuDTO;

public interface MenuService {
    Page<MenuDTO> getMenus(Pageable pageable);

    MenuDTO addMenu(MenuDTO menuDTO);

    void deleteMenu(Long id);

    MenuDTO updateMenu(Long id, MenuDTO menuDTO);

    MenuDTO getMenuById(Long id);
}
