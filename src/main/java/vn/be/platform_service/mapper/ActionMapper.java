package vn.be.platform_service.mapper;

import org.mapstruct.*;
import vn.be.platform_service.dto.ActionDto;
import vn.be.platform_service.entity.Action;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    ActionDto toDTO(Action action);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Action toEntity(ActionDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ActionDto dto, @MappingTarget Action action);
}
