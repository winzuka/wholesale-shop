package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Entity to DTO Mapping: map customer.customer_id to customer_id in DTO
    @Mapping(source = "customer.customer_id", target = "customer_id")
    OrderDto orderToOrderDto(Orders orders);

    // DTO to Entity Mapping: ignore customer, it's set in the service layer
    @Mapping(target = "customer", ignore = true)
    Orders orderDtoToOrder(OrderDto orderDto);
}
