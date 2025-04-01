package com.wholesaleshop.demo_wholesale_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    Integer customer_id;
    String customer_name;
    String customer_address;
    String customer_email;
    String customer_phone;
}
