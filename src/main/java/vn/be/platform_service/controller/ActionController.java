package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.be.platform_service.dto.ActionDto;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.service.impl.ActionServiceImpl;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionServiceImpl actionService;

    @PostMapping
    public ApiResponse<ActionDto> create(@RequestBody ActionDto actionDTO) {
        return ApiResponse.success(actionService.create(actionDTO));
    }
}
