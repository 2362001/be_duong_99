package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.be.platform_service.entity.Action;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findAllByStatus(Integer status);
}
