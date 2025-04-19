package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT * FROM product WHERE " +
            "product_name LIKE %:query%",
            nativeQuery = true)
    List<Product> searchProducts(@Param("query") String query);

    Page<Product> findAll(Pageable pageable);

}
