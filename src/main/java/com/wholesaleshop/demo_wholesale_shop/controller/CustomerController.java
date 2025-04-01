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

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);
        return ResponseEntity.ok(savedCustomer);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDto> deleteCustomer(@PathVariable Integer id) {
        CustomerDto deletedCustomer = customerService.deleteCustomer(id);

        if(deletedCustomer == null) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(deletedCustomer);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Integer id) {
//        CustomerDto customer = customerService.searchCustomer(id);
//
//        if(customer == null) {
//            return ResponseEntity.notFound().build();
//        }else{
//            return ResponseEntity.ok(customer);
//        }
//    }

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDto> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomers(@RequestParam String query) {
        List<CustomerDto> customers = customerService.searchCustomers(query);

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }
}
