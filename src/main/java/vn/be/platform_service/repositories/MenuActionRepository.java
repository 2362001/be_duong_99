package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.be.platform_service.entity.MenuAction;

public interface MenuActionRepository extends JpaRepository<MenuAction, Long> {
}
