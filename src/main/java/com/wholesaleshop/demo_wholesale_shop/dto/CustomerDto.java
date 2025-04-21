package com.wholesaleshop.demo_wholesale_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private
    Integer customer_id;

    private String customer_name;
    private String customer_address;
    private String customer_email;
    private String customer_phone;
}
