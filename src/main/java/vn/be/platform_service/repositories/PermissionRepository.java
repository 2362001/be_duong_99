package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.be.platform_service.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long > {
}
