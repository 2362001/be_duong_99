package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.be.platform_service.entity.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
