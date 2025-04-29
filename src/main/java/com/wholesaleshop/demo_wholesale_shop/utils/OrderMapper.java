package com.wholesaleshop.demo_wholesale_shop.utils;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "customer.customer_id", target = "customer_id")
    OrderDto orderToOrderDto(Orders orders);

    @Mapping(target = "customer", ignore = true)
    @Mapping(source = "orderDate", target = "orderDate")
    Orders orderDtoToOrder(OrderDto orderDto);
}
