package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.be.platform_service.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long > {
}
