package vn.be.platform_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.WhitelistRequest;
import vn.be.platform_service.entity.Whitelist;
import vn.be.platform_service.service.impl.WhitelistService;

@RestController
@RequestMapping("/api/whitelist")
@RequiredArgsConstructor
public class WhitelistController {

//    API THÊM 1 ENDPOINT VÀO WHITELIST

    private final WhitelistService whitelistService;

    @PostMapping
    public ApiResponse<Whitelist> createWhitelist(@Valid @RequestBody WhitelistRequest whitelist) {
        return ApiResponse.success(whitelistService.createWhitelist(whitelist));
    }
}
