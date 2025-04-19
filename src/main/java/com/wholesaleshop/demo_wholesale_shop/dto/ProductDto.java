package com.wholesaleshop.demo_wholesale_shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer product_id;

    String product_name;
    Double product_price;
    Integer stock_quantity;

    Integer supplier_id;
}
