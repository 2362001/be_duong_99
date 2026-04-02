package vn.be.platform_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.be.platform_service.entity.Whitelist;

import java.util.List;

public interface WhitelistRepository extends JpaRepository<Whitelist,Long> {
    List<Whitelist> findByEnabledTrue();
}
