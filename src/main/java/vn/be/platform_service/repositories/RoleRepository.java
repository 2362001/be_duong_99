package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.be.platform_service.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long > {
}
