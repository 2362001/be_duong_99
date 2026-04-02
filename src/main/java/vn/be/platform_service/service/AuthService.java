package vn.be.platform_service.service;

import vn.be.platform_service.dto.auth.LoginRequest;
import vn.be.platform_service.dto.auth.LoginResponse;
import vn.be.platform_service.dto.req.RegisterRequest;
import vn.be.platform_service.dto.res.RegisterResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    RegisterResponse register(RegisterRequest registerRequest);
}
