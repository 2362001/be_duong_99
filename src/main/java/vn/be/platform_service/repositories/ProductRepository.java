package vn.be.platform_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.be.platform_service.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //native query , query , jdbc
    @Query(value = "select  * from product where status =:status",
            countQuery = "select count(*) from product where status = :status",
            nativeQuery = true)
    Page<Product> getAllProductActive(int status, Pageable pageable);
}
