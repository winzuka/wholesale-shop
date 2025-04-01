package com.wholesaleshop.demo_wholesale_shop.service;


import com.wholesaleshop.demo_wholesale_shop.dto.CustomerDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomerService {

    CustomerDto saveCustomer(CustomerDto customerDto);
    CustomerDto updateCustomer(CustomerDto customerDto);
    CustomerDto deleteCustomer(Integer customerId);
    CustomerDto searchCustomer(Integer customerId);
    List<CustomerDto> getAllCustomers();
}
