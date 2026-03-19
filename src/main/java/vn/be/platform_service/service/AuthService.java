package vn.be.platform_service.service;

import vn.be.platform_service.dto.auth.LoginRequest;
import vn.be.platform_service.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
