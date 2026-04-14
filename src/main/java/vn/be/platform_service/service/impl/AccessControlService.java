package vn.be.platform_service.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import vn.be.platform_service.dto.SimpleRequest;
import vn.be.platform_service.entity.Action;
import vn.be.platform_service.repositories.ActionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service cốt lõi RBAC — cache in-memory danh sách allowPaths của từng Action.
 * Port từ kntc-admin AccessControlInMemoryService.
 * <p>
 * Kiểm tra quyền truy cập 3 lớp:
 * 1. Whitelist: không cần đăng nhập và không cần token (action code = WHITELIST_API)
 * 2. Authenticated: chỉ cần đăng nhập (action code = AUTHENTICATED_API) không cần phân quyền
 * 3. RBAC: kiểm tra actionCodes của user match với allowPaths
 */


@Service
@RequiredArgsConstructor
@Slf4j
public class AccessControlService {

    public static final String WHITELIST_ACTION_CODE = "WHITELIST_API";
    public static final String AUTHENTICATED_ACTION_CODE = "AUTHENTICATED_API";

    /**
     * Map: actionCode → Set<SimpleRequest> (các API path patterns đã parse)
     */
    private final Map<String, Set<SimpleRequest>> allowPathsByActionCode = new ConcurrentHashMap<>();
    private final AntPathMatcher antMatcher = new AntPathMatcher();

    private final ActionRepository actionRepository;

    /**
     * Khởi tạo cache khi ứng dụng chạy.
     * Nếu bảng actions chưa tồn tại → log warning và tiếp tục khởi động.
     */
    @PostConstruct
    public void init() {
        try {
            reload();
        } catch (Exception e) {
            log.warn("AccessControl: Không thể load cache khi khởi động (bảng chưa tồn tại?). " + "Gọi POST /api/actions/reload sau khi tạo bảng. Lỗi: {}", e.getMessage());
        }
    }


    /**
     * Reload toàn bộ cache từ DB
     */
    public void reload() {
        allowPathsByActionCode.clear();
        List<Action> activeActions = actionRepository.findAllByStatus(1);
        //[/ dòng entity 1
        // // dòng entity 2
        // ]
        for (Action action : activeActions) {
            Set<SimpleRequest> matchers = parsePathsToMatchers(action.getAllowPaths());
            allowPathsByActionCode.put(action.getCode(), matchers);
        }
        log.info("AccessControl: Loaded {} actions into cache", activeActions.size());
        allowPathsByActionCode.forEach((code, paths) -> log.debug("  ACTION [{}] → {} paths", code, paths.size()));


//    allowPathsByActionCode = {
//        "WHITELIST_API"      → { SimpleRequest("/api/auth/**"), ... }
//        "AUTHENTICATED_API"  → { SimpleRequest("/api/notifications/**"), ... }
//        "VIEW_HO_SO"         → { SimpleRequest("GET", "/api/ho-so/**"), ... }
//        "DELETE_USER"        → { SimpleRequest("DELETE", "/api/users/*"), ... }
//  ...
//}
    }

    /**
     * Parse chuỗi allowPaths thành Set<SimpleRequest>.
     * Hỗ trợ phân cách bằng: dấu phẩy (,) HOẶC xuống dòng (\n) HOẶC cả hai.
     * Format mỗi entry: "METHOD:PATH" hoặc "PATH" (match mọi method)
     * VD: "GET:/api/users/**" hoặc "/api/auth/**"
     */
    private Set<SimpleRequest> parsePathsToMatchers(String paths) {
        if (!StringUtils.hasText(paths)) {
            return Set.of();
        }
        // Split bằng cả dấu phẩy và xuống dòng
        return Arrays.stream(paths.split("[,\\n]+")).map(String::trim).filter(StringUtils::hasText).map(path -> {
            path = path.replaceAll("\\p{Cntrl}", "");
            String[] parts = path.split(":", 2);
            if (parts.length == 2) {
                return new SimpleRequest(parts[1].trim(), parts[0].trim());
            }
            return new SimpleRequest(path);
        }).collect(Collectors.toSet());
    }

    //hàm kiểm tra có thuộc action code whitelist hay không

    private static final List<String> HARDCODED_WHITELIST = List.of("/api/auth/**");

    /**
     * Lớp 1: Kiểm tra path có nằm trong WHITELIST không (không cần token)
     */
    public boolean isWhiteList(String method, String uri) {
        // Luôn cho qua các path hardcoded
        if (HARDCODED_WHITELIST.stream().anyMatch(pattern -> antMatcher.match(pattern, uri))) {
            return true;
        }
        SimpleRequest request = new SimpleRequest(uri, method);
        return allowPathsByActionCode.getOrDefault(WHITELIST_ACTION_CODE, Set.of()).stream().anyMatch(matcher -> matcher.matches(request));
    }

    private static final List<String> HARDCODED_AUTHENTICATED = List.of("/api/actions/reload", "/api/notifications/**"     // SSE stream + notification API
    );


    /**
     * Lớp 2: Kiểm tra path chỉ cần đăng nhập (authenticated-only)
     */
    public boolean isAuthenticated(String uri, String method) {
        if (HARDCODED_AUTHENTICATED.stream().anyMatch(pattern -> antMatcher.match(pattern, uri))) {
            return true;
        }

        SimpleRequest request = new SimpleRequest(uri, method);
        return allowPathsByActionCode.getOrDefault(AUTHENTICATED_ACTION_CODE, Set.of()).stream().anyMatch(matcher -> matcher.matches(request));
    }


}
