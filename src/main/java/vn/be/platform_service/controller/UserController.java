package vn.be.platform_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import vn.be.platform_service.constant.ApiConstant;
import vn.be.platform_service.dto.ApiResponse;
import vn.be.platform_service.dto.UserDTO;
import vn.be.platform_service.service.UserService;

@RestController
@RequestMapping(ApiConstant.API_PREFIX + "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<Page<UserDTO>> getUsers(Pageable pageable) {
        return ApiResponse.success(userService.getUsers(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    @PostMapping
    public ApiResponse<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return ApiResponse.success(userService.addUser(userDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ApiResponse.success(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}
