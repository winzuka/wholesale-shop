package com.wholesaleshop.demo_wholesale_shop.controller;

import com.wholesaleshop.demo_wholesale_shop.dto.CustomerDto;
import com.wholesaleshop.demo_wholesale_shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customer-related operations.
 * Base URL: /api/v1/customer
 */
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    /**
     * Create a new customer.
     *
     * @param customerDto DTO containing customer details.
     * @return ResponseEntity with the saved customer.
     */

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

    /**
     * Update an existing customer.
     *
     * @param customerDto DTO with updated customer details.
     * @param id          ID of the customer to update.
     * @return ResponseEntity with the updated customer, or 404 if not found.
     */

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable Integer id) {
        customerDto.setCustomer_id(id);
        CustomerDto updatedCustomer = customerService.updateCustomer(customerDto);

        if(updatedCustomer == null) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(updatedCustomer);
        }
    }

    /**
     * Delete a customer by ID.
     *
     * @param id ID of the customer to delete.
     * @return ResponseEntity with the deleted customer, or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Integer id) {
        CustomerDto deletedCustomer = customerService.deleteCustomer(id);

        if(deletedCustomer == null) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(deletedCustomer);
        }
    }

    /**
     * Get a paginated list of all customers.
     *
     * @param page Page number (default is 0).
     * @param size Number of customers per page (default is 5).
     * @return Paginated list of customers.
     */
    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    /**
     * Search for customers by a given query.
     *
     * @param query Search keyword.
     * @return List of matching customers or 204 No Content if none found.
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam String query) {
        List<CustomerDto> customers = customerService.searchCustomers(query);

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }
}
