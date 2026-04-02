package vn.be.platform_service.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.req.MenuActionRequest;
import vn.be.platform_service.entity.MenuAction;
import vn.be.platform_service.repositories.ActionRepository;
import vn.be.platform_service.repositories.MenuActionRepository;
import vn.be.platform_service.repositories.MenuRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuActionServiceImpl {

    private final MenuActionRepository menuActionRepository;
    private final MenuRepository menuRepository;
    private final ActionRepository actionRepository;

    @Transactional
    public MenuAction assignActionForMenu(@RequestBody MenuActionRequest request) {

        //check kiểm tra xem menu và action có tồn tại hay không
        menuRepository.findById(request.getMenuId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        actionRepository.findById(request.getActionId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        MenuAction menuAction = new MenuAction();
        menuAction.setMenuId(request.getMenuId());
        menuAction.setActionId(request.getActionId());

        // Nếu ko truyền actionCode thì tự lấy từ Action
        // nếu nhwu không gán tay action vào menu thì mặc định gán tất cả action từ db vào menu đó để được full quyền thao tác trong menu
        // pending để làm sau

        MenuAction saved = menuActionRepository.save(menuAction);

        log.info("Menu Action assigned successfully", request.getMenuId(), request.getActionId(), saved.getActionCode());

        return saved;
    }

    @Transactional
    public List<MenuAction> getActionByMenuId(@PathVariable Long menuId){
        menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return menuActionRepository.findByMenuId(menuId);
    }

    @Transactional
    public void deleteActionById(@PathVariable Long id){
        menuActionRepository.deleteById(id);
    }

    @Transactional
    public MenuAction getActionById(@PathVariable Long id){
        return menuActionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
