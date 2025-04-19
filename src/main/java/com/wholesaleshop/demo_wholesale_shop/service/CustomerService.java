package com.wholesaleshop.demo_wholesale_shop.service;


import com.wholesaleshop.demo_wholesale_shop.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerService {

    CustomerDto saveCustomer(CustomerDto customerDto);
    CustomerDto updateCustomer(CustomerDto customerDto);
    CustomerDto deleteCustomer(Integer customerId);
    Page<CustomerDto> getAllCustomers(Pageable pageable);
    List<CustomerDto> searchCustomers(String query);
}
