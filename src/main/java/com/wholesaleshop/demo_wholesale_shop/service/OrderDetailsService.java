package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;

import java.util.List;

public interface OrderDetailsService {

    OrderDetailsDto saveOrderDetails(OrderDetailsDto orderDetailsDto);
    OrderDetailsDto getOrderDetailsById(Integer id);
    List<OrderDetailsDto> getAllOrderDetails();
    void deleteOrderDetails(Integer id);
    OrderDetailsDto updateOrderDetails(Integer id, OrderDetailsDto orderDetailsDto);
}
