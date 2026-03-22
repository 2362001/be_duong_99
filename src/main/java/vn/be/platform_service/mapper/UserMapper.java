package vn.be.platform_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vn.be.platform_service.dto.UserDTO;
import vn.be.platform_service.entity.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
    List<UserDTO> toDTOs(List<User> users);
}
