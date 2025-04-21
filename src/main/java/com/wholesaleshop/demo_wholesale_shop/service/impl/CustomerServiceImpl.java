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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepo customerRepo; // Injecting the Customer Repository to interact with the database.
    @Autowired
    CustomerMapper customerMapper; // Injecting the CustomerMapper to convert between DTO and Entity.

    /**
     * Save a new customer in the database.
     * @param customerDto DTO containing customer data.
     * @return The saved CustomerDto.
     */
    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {

        // Convert DTO to Entity.
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);

        // Save the customer in the database.
        Customer savedCustomer = customerRepo.save(customer);

        // Convert the saved entity back to DTO and return it.
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    /**
     * Update an existing customer's details.
     * @param customerDto DTO containing updated customer data.
     * @return The updated CustomerDto, or null if the customer doesn't exist.
     */
    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {

        // Check if the customer exists in the database.
        Optional<Customer> existingCustomerOpt = customerRepo.findById(customerDto.getCustomer_id());

        if (existingCustomerOpt.isPresent()) {
            // Update the customer's information.
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setCustomer_name(customerDto.getCustomer_name());
            existingCustomer.setCustomer_address(customerDto.getCustomer_address());
            existingCustomer.setCustomer_email(customerDto.getCustomer_email());
            existingCustomer.setCustomer_phone(customerDto.getCustomer_phone());

            // Save the updated customer.
            Customer updatedCustomer = customerRepo.save(existingCustomer);

            // Convert updated entity back to DTO and return it.
            return customerMapper.customerToCustomerDto(updatedCustomer);
        }

        return null;
    }

    /**
     * Delete a customer from the database by ID.
     * @param customerId ID of the customer to be deleted.
     * @return The deleted CustomerDto, or null if the customer doesn't exist.
     */
    @Override
    public CustomerDto deleteCustomer(Integer customerId) {
        // Find the customer by ID.
        Optional<Customer> customerOpt = customerRepo.findById(customerId);

        if (customerOpt.isPresent()) {
            // Delete the customer from the database.
            customerRepo.deleteById(customerId);

            // Convert the deleted entity back to DTO and return it.
            return customerMapper.customerToCustomerDto(customerOpt.get());
        }

        return null;
    }

    /**
     * Get all customers with pagination.
     * @param pageable Pageable object containing pagination information.
     * @return A page of CustomerDto objects.
     */
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        // Fetch the customers from the database with pagination.
        Page<Customer> customerPage = customerRepo.findAll(pageable);

        // Convert the entities in the page to DTOs and return the page.
        return customerPage.map(customerMapper::customerToCustomerDto);
    }

    /**
     * Search customers based on a query.
     * @param query The search query to filter customers.
     * @return A list of CustomerDto objects that match the query.
     */
    public List<CustomerDto> searchCustomers(String query) {
        // Fetch the list of customers based on the query.
        List<Customer> customers = customerRepo.searchCustomers(query);

        // Convert the list of entities to DTOs and return.
        return customers.stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

}
