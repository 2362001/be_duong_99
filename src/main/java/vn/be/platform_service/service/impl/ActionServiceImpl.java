package vn.be.platform_service.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.be.platform_service.dto.ActionDto;
import vn.be.platform_service.entity.Action;
import vn.be.platform_service.mapper.ActionMapper;
import vn.be.platform_service.repositories.ActionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionServiceImpl {

    private final ActionMapper actionMapper;
    private final ActionRepository actionRepository;

    @Transactional
    public ActionDto create(ActionDto actionDTO) {
        Action action = actionMapper.toEntity(actionDTO);
        Action saved = actionRepository.save(action);
//        accessControlService.reload();
        log.info("Đã tạo action: {} ({})", saved.getName(), saved.getCode());
        return actionMapper.toDTO(saved);
    }
}
