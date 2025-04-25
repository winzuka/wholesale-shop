package com.wholesaleshop.demo_wholesale_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer customer_id;

    @NotBlank(message = "Customer name is required")
    private String customer_name;

    @NotBlank(message = "Address is required")
    private String customer_address;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String customer_email;

    @NotBlank(message = "Phone number is required")
    private String customer_phone;
}
