package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Orders, Integer> {

    @Query(value = "SELECT * FROM orders WHERE " +
            "orderDate LIKE %:query% OR " +
            "orderId LIKE %:query%",
            nativeQuery = true)
    List<Orders> searchOrders(@Param("query") String query);
    Optional<Orders> findById(Integer orderId);
    Page<Orders> findAll(Pageable pageable);

}
