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

    @Query(value = "SELECT " +
            "orders.orderId, " +
            "orders.customer_id, " +
            "orders.orderDate, " +
            "orders.order_price, " +
            "orderdetails.quantity, " +
            "product.product_id, " +
            "product.product_name, " +
            "payment.paid_amount, " +
            "payment.payment_method " +
            "FROM orders " +
            "LEFT JOIN orderdetails ON orders.orderId = orderdetails.order_id " +
            "LEFT JOIN product ON orderdetails.product_id = product.product_id " +
            "LEFT JOIN payment ON orders.orderId = payment.order_id " +
            "WHERE " +
            "orders.orderDate LIKE CONCAT('%', :keyword, '%') OR " +
            "CAST(orders.orderId AS CHAR) LIKE CONCAT('%', :keyword, '%') OR " +
            "CAST(orderdetails.quantity AS CHAR) LIKE CONCAT('%', :keyword, '%') OR " +
            "CAST(product.product_id AS CHAR) LIKE CONCAT('%', :keyword, '%') OR " +
            "CAST(payment.paid_amount AS CHAR) LIKE CONCAT('%', :keyword, '%') OR " +
            "payment.payment_method LIKE CONCAT('%', :keyword, '%')",
            nativeQuery = true)

    List<Orders> searchOrders(@Param("keyword") String query);
    Optional<Orders> findById(Integer orderId);
    Page<Orders> findAll(Pageable pageable);

}
