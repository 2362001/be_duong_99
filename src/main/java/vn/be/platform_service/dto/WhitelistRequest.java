package vn.be.platform_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class WhitelistRequest {

    private String pattern;

    private String description;

    @Data
    public static class BulkRequest {
        private List<String> patterns;
        private String description;
    }
}
