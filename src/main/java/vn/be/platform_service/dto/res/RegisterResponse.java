package vn.be.platform_service.dto.res;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private Integer age;
}
