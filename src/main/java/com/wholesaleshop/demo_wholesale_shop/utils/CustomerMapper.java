package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.CustomerDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Entity to DTO Mapping
    CustomerDto customerToCustomerDto(Customer customer);

    // DTO to Entity Mapping (Ignoring Orders)
    @Mapping(target = "orders", ignore = true)
    Customer customerDtoToCustomer(CustomerDto customerDto);

}
