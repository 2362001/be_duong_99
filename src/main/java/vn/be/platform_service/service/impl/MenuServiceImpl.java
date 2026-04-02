package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.MenuDTO;
import vn.be.platform_service.entity.Menu;
import vn.be.platform_service.mapper.MenuMapper;
import vn.be.platform_service.repositories.MenuRepository;
import vn.be.platform_service.service.MenuService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    final MenuRepository menuRepository;
    final MenuMapper menuMapper;

    @Override
    public Page<MenuDTO> getMenus(Pageable pageable) {
        return menuRepository.findAll(pageable).map(menuMapper::toDTO);
    }

    @Override
    public MenuDTO addMenu(MenuDTO menuDTO) {
        Menu menu = menuMapper.toEntity(menuDTO);
        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDTO(savedMenu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);

    }

    @Override
    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(menuDTO.getName() != null){
            existingMenu.setCode(menuDTO.getCode());
        }

        if(menuDTO.getIcon() != null){
            existingMenu.setIcon(menuDTO.getIcon());
        }

        if(menuDTO.getPath() != null){
            existingMenu.setPath(menuDTO.getPath());
        }

        if(menuDTO.getParentId() != null){
            existingMenu.setParentId(menuDTO.getParentId());
        }

        Menu newMenu = menuRepository.save(existingMenu);
        return menuMapper.toDTO(newMenu);
    }

    @Override
    public MenuDTO getMenuById(Long id) {
        Menu existingMenu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return menuMapper.toDTO(existingMenu);
    }
}
