package vn.be.platform_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

/**
 * Đại diện cho 1 request đơn giản (URI + method)
 * dùng để matching với allowPaths trong Action.
 */
@Data
@NoArgsConstructor
public class SimpleRequest {
    private String method;
    private String uri;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public SimpleRequest(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    public SimpleRequest(String uri) {
        this.uri = uri;
    }

    /**
     * So khớp request này (pattern) với request khác.
     * Hỗ trợ AntPath wildcard: /api/users/**, /api/products/*
     * Hỗ trợ method matching: nếu pattern không chỉ định method → match mọi method
     */
    public boolean matches(SimpleRequest otherRequest) {
        boolean uriMatches = PATH_MATCHER.match(this.uri, otherRequest.uri);
        if (!StringUtils.hasText(this.method)) {
            return uriMatches;
        }
        return uriMatches && this.method.equalsIgnoreCase(otherRequest.method);
    }
}
