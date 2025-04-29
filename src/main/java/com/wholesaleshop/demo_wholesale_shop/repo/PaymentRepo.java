package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

    @Query(value = "SELECT * FROM payment WHERE " +
            "payment_method LIKE %:query% OR " +
            "payment_id LIKE %:query%",
            nativeQuery = true)

    List<Payment> searchPayments(@Param("query") String query);

    Page<Payment> findAll(Pageable pageable);

    Optional<Payment> findByOrders(Orders Orders);
}
