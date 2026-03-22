package vn.be.platform_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.be.platform_service.dto.UserDTO;

public interface UserService {
    Page<UserDTO> getUsers(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO addUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}
