package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.CustomerDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import com.wholesaleshop.demo_wholesale_shop.repo.CustomerRepo;
import com.wholesaleshop.demo_wholesale_shop.service.CustomerService;
import com.wholesaleshop.demo_wholesale_shop.utils.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    CustomerMapper customerMapper;


    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        Customer savedCustomer = customerRepo.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        Optional<Customer> existingCustomerOpt = customerRepo.findById(customerDto.getCustomer_id());

        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setCustomer_name(customerDto.getCustomer_name());
            existingCustomer.setCustomer_address(customerDto.getCustomer_address());
            existingCustomer.setCustomer_email(customerDto.getCustomer_email());
            existingCustomer.setCustomer_phone(customerDto.getCustomer_phone());

            Customer updatedCustomer = customerRepo.save(existingCustomer);
            return customerMapper.customerToCustomerDto(updatedCustomer);
        }

        return null;
    }

    @Override
    public CustomerDto deleteCustomer(Integer customerId) {
        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isPresent()) {
            customerRepo.deleteById(customerId);
            return customerMapper.customerToCustomerDto(customerOpt.get());
        }

        return null;
    }

    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepo.findAll(pageable);
        return customerPage.map(customerMapper::customerToCustomerDto);
    }

    public List<CustomerDto> searchCustomers(String query) {
        List<Customer> customers = customerRepo.searchCustomers(query);
        return customers.stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

}
