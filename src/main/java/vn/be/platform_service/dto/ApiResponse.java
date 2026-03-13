package vn.be.platform_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private int status;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().status(200).data(data).message("Success").build();
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder().status(status).message(message).build();
    }
}
