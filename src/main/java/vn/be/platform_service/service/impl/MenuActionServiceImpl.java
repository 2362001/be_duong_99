package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.req.MenuActionRequest;
import vn.be.platform_service.entity.MenuAction;
import vn.be.platform_service.repositories.ActionRepository;
import vn.be.platform_service.repositories.MenuActionRepository;
import vn.be.platform_service.repositories.MenuRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuActionServiceImpl {

    private final MenuActionRepository menuActionRepository;
    private final MenuRepository menuRepository;
    private final ActionRepository actionRepository;

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

}
