package vn.be.platform_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Token Blacklist Service — quản lý danh sách token bị thu hồi bằng Redis.
 * <p>
 * Cơ chế:
 * 1. Blacklist TOKEN: Lưu token vào Redis với TTL = thời gian còn lại của token.
 * Khi token hết hạn tự nhiên → Redis tự xóa key → tiết kiệm bộ nhớ.
 * <p>
 * 2. Revoke USER: Lưu timestamp "revoked_at" cho user.
 * Mọi token được cấp TRƯỚC thời điểm này đều bị coi là không hợp lệ.
 * → Cho phép thu hồi TẤT CẢ token của user mà không cần biết token cụ thể.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "security:token:blacklist:";
    private static final String USER_REVOKED_PREFIX = "security:user:revoked:";

    /**
     * Thêm token vào blacklist.
     * Token sẽ tự động bị xóa khỏi Redis khi hết hạn tự nhiên.
     *
     * @param token          JWT token cần blacklist
     * @param expirationDate thời điểm hết hạn của token
     */
    public void blacklistToken(String token, Date expirationDate) {
        long ttlMillis = expirationDate.getTime() - System.currentTimeMillis();
        if (ttlMillis <= 0) {
            log.debug("Token đã hết hạn, không cần blacklist");
            return;
        }

        String key = TOKEN_BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "revoked", ttlMillis, TimeUnit.MILLISECONDS);
        log.info("Đã blacklist token (TTL: {}s)", ttlMillis / 1000);
    }

    /**
     * Kiểm tra token có bị blacklist không.
     */
    public Boolean isTokenBlacklisted(String token) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Thu hồi tất cả token của user.
     * Mọi token được cấp TRƯỚC thời điểm này sẽ bị coi là không hợp lệ.
     *
     * @param username           username cần revoke
     * @param maxTokenLifetimeMs thời gian sống tối đa của token (dùng làm TTL cho key)
     */
    public void revokeAllTokenOfUser(String username, long maxTokenLifetimeMs) {
        String key = USER_REVOKED_PREFIX + username;

        long revokeAt = System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, revokeAt, maxTokenLifetimeMs, TimeUnit.MILLISECONDS);
    }
}
