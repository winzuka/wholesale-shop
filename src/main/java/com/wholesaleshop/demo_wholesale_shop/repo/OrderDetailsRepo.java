package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {
}
