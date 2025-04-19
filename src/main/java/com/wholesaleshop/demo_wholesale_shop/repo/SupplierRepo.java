package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {

    @Query(value = "SELECT * FROM supplier WHERE " +
            "supplier_name LIKE %:query% OR " +
            "supplier_address LIKE %:query% OR " +
            "supplier_phone LIKE %:query%",
            nativeQuery = true)
    List<Product> searchProducts(@Param("query") String query);

    Page<Supplier> findAll(Pageable pageable);
}
