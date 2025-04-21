package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.OrderDetails;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {

    @Query(value = "SELECT * FROM order_details WHERE " +
            "orderDetailsId LIKE %:query%",
            nativeQuery = true)
    List<OrderDetails> searchOrderDetails(@Param("query") String query);
    List<OrderDetails> findByOrders(Orders orders);

    Page<OrderDetails> findAll(Pageable pageable);
}
