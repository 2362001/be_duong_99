package vn.be.platform_service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.be.platform_service.entity.User;
import vn.be.platform_service.service.impl.AccessControlService;

/**
 * Filter kiểm soát phân quyền theo 3 lớp dựa trên AccessControlService:
 *
 * Lớp 1 - WHITELIST_API : Không cần token → cho qua
 * Lớp 2 - AUTHENTICATED_API : Chỉ cần đăng nhập → cho qua nếu đã auth
 * Lớp 3 - RBAC             : Kiểm tra action codes của user từ role_mapping
 *
 * Filter này chạy SAU JwtAuthenticationFilter (user đã được load vào SecurityContext).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessControlFilter extends OncePerRequestFilter {
    private final AccessControlService accessControlService;
    private final RoleMappingRepository roleMappingRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getServletPath();
        String method = request.getMethod();

        // ========== Lớp 1: WHITELIST — không cần token ==========
        if (accessControlService.isWhiteList(uri, method)) {
            log.debug("AccessControl [WHITELIST] {} {}", method, uri);
            filterChain.doFilter(request, response);
            return;
        }

        // ========== Lấy thông tin authentication ==========
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User;

        // ========== Lớp 2: AUTHENTICATED — chỉ cần đăng nhập ==========
        if (accessControlService.isAuthenticated(uri, method)) {
            if (isAuthenticated) {
                log.debug("AccessControl [AUTHENTICATED] {} {}", method, uri);
                filterChain.doFilter(request, response);
            } else {
                log.warn("AccessControl [DENIED-401] {} {} — chưa đăng nhập", method, uri);
                sendUnauthorized(response, "Bạn cần đăng nhập để truy cập");
            }
            return;
        }

        // ========== Lớp 3: RBAC — kiểm tra action codes ==========
        if (!isAuthenticated) {
            log.warn("AccessControl [DENIED-401] {} {} — chưa đăng nhập", method, uri);
            sendUnauthorized(response, "Bạn cần đăng nhập để truy cập");
            return;
        }

        // Tại đây isAuthenticated=true nên authentication và principal không thể null
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        if (!(principal instanceof User user)) {
            sendUnauthorized(response, "Phiên đăng nhập không hợp lệ");
            return;
        }

        // Tự động cho phép nếu User có role ADMIN (Super Admin bypass)
        if (user.getRoleNames() != null && user.getRoleNames().contains("ADMIN")) {
            log.debug("AccessControl [SUPER-ADMIN] {} {} — user={}", method, uri, user.getUsername());
            filterChain.doFilter(request, response);
            return;
        }

        Set<String> actionCodes = roleMappingRepository.findActionCodesByUserId(user.getId());

        if (accessControlService.isAuthorized(uri, method, actionCodes)) {
            log.debug("AccessControl [AUTHORIZED] {} {} — user={}, actionCodes={}",
                    method, uri, user.getUsername(), actionCodes);
            filterChain.doFilter(request, response);
        } else {
            log.warn("AccessControl [DENIED-403] {} {} — user={}, actionCodes={}",
                    method, uri, user.getUsername(), actionCodes);
            sendForbidden(response, "Bạn không có quyền truy cập");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + message + "\",\"data\":null}");
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + message + "\",\"data\":null}");
    }
}
