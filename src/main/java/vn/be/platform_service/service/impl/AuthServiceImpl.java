package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.be.platform_service.dto.auth.LoginRequest;
import vn.be.platform_service.dto.auth.LoginResponse;
import vn.be.platform_service.entity.User;
import vn.be.platform_service.repositories.UserRepository;
import vn.be.platform_service.service.AuthService;
import vn.be.platform_service.utils.JwtUtil;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_KEY_PREFIX = "user:";
    private static final Duration USER_SESSION_TTL = Duration.ofHours(1);

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
//        tìm user  theo username
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mk"));

//        kiểm tra xem tài khoản có bị inactive không
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản đã bị vô hiệu hóa");
        }

//        so sánh password
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
        }

//        sinh ra accesstoken và refreshtoken
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        saveUserToRedis(user, accessToken);

        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .fullName(user.getFullName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToRedis(User user, String accessToken) {
        String key = USER_KEY_PREFIX + user.getUsername();
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("id", String.valueOf(user.getId()));
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("fullName", user.getFullName());
        userInfo.put("name", user.getName());
        userInfo.put("avatarUrl", user.getAvatarUrl());
        userInfo.put("enabled", String.valueOf(user.getEnabled()));
        userInfo.put("accessToken", accessToken);
        redisTemplate.opsForHash().putAll(key, userInfo);
        redisTemplate.expire(key, USER_SESSION_TTL);
    }
}
