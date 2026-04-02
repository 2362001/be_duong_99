package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.req.MenuActionRequest;
import vn.be.platform_service.entity.MenuAction;
import vn.be.platform_service.service.impl.MenuActionServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/menu-actions")
@RequiredArgsConstructor
public class MenuActionController {

    private final MenuActionServiceImpl menuActionService;

//    gán action vào menu
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MenuAction> assignActionForMenu(@RequestBody MenuActionRequest request) {
        return ApiResponse.success(menuActionService
                .assignActionForMenu(request));
    }

    @GetMapping("/get-action-by-menu/{menuId}")
    public ApiResponse<List<MenuAction>> getActionByMenuId(@PathVariable Long menuId){
        return ApiResponse.success(menuActionService.getActionByMenuId(menuId));
    }

    @DeleteMapping("/delete-action-by-id/{id}")
    public void deleteActionById(@PathVariable Long id){
        menuActionService.deleteActionById(id);
    }

    @GetMapping("/get-action-by-id/{id}")
    public ApiResponse<MenuAction> getActionById(@PathVariable Long id){
        return ApiResponse.success(menuActionService.getActionById(id));
    }

}
