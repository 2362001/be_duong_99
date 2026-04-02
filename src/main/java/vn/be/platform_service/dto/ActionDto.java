package vn.be.platform_service.dto;

import java.time.LocalDateTime;

public class ActionDto {
    private Long id;
    private String code;
    private String name;
    private String allowPaths;
    private String logPaths;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
