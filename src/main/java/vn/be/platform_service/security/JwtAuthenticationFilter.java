package vn.be.platform_service.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.be.platform_service.service.impl.TokenBlacklistService;

import java.io.IOException;
import java.util.Date;

/**
 * JWT Authentication Filter — xác thực + kiểm tra blacklist:
 * <p>
 * 1. Parse JWT token từ header Authorization
 * 2. Kiểm tra token có bị blacklist/revoke không
 * 3. Validate token (chữ ký + hết hạn)
 * 4. Load UserDetails và set vào SecurityContext của java spring boot
 */

@Component
@RequiredArgsConstructor
@Slf4j

//học tính kế thừa của java
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            // Nếu username hợp lệ và chưa được xác thực
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // ===== CHECK BLACKLIST =====
                // 1. Token cụ thể bị blacklist (logout)
                if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                    log.warn("Token bị blacklist cho user '{}'", username);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"Token đã bị thu hồi. Vui lòng đăng nhập lại.\"}");
                    return;
                }

                // viết check thêm xem token có bị revoke hay chưa
                // viết ở đây nhé
                //

                // ===== VALIDATE TOKEN =====
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    setAuthentication(userDetails, request);
                    log.debug("JWT: Xác thực thành công cho user '{}'", username);
                }
            }
        } catch (Exception e) {
            log.error("Không thể xác thực JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

}
