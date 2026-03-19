package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.be.platform_service.dto.auth.LoginRequest;
import vn.be.platform_service.dto.auth.LoginResponse;
import vn.be.platform_service.entity.User;
import vn.be.platform_service.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository
    }
}
