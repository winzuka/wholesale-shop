package com.wholesaleshop.demo_wholesale_shop.repo;

import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {
}
