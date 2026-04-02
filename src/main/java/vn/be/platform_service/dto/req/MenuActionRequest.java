package vn.be.platform_service.dto.req;

import lombok.Data;

@Data
public class MenuActionRequest {
    private Long menuId;
    private Long actionId;
    private String actionCode;
}
