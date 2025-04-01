package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    @Query(value = "SELECT * FROM customer WHERE " +
            "customer_name LIKE %:query% OR " +
            "customer_address LIKE %:query% OR " +
            "customer_email LIKE %:query% OR " +
            "customer_phone LIKE %:query%",
            nativeQuery = true)
    List<Customer> searchCustomers(@Param("query") String query);

    Page<Customer> findAll(Pageable pageable);
}
