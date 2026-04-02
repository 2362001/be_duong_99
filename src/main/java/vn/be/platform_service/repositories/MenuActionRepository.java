package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.be.platform_service.entity.MenuAction;

import java.util.List;

public interface MenuActionRepository extends JpaRepository<MenuAction, Long> {
    List<MenuAction> findByMenuId(Long menuId);
}
