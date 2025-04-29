package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Invoice;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {

    @Query(value = "SELECT * FROM invoice WHERE " +
            "orderId LIKE %:query% OR " +
            "payment_id LIKE %:query%",
            nativeQuery = true)
    List<Invoice> searchInvoices(@Param("query") String query);

    Page<Invoice> findAll(Pageable pageable);

    Optional<Invoice> findByOrders(Orders orders);


}
