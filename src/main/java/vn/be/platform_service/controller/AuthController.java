package vn.be.platform_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.be.platform_service.constant.ApiConstant;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.auth.LoginRequest;
import vn.be.platform_service.dto.auth.LoginResponse;
import vn.be.platform_service.security.JwtService;
import vn.be.platform_service.service.AuthService;
import vn.be.platform_service.service.impl.TokenBlacklistService;

import static vn.be.platform_service.constant.ApiConstant.API_PREFIX;

@RestController
@RequestMapping(ApiConstant.API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.success(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token, jwtService.getExpiration(token));
        }

        return ApiResponse.success(null);
    }
}
