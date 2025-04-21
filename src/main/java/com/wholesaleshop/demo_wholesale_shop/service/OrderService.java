package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto);
    OrderDto updateOrder(OrderDto orderDto);
    OrderDto deleteOrder(Integer orderId);
    List<OrderDto> searchOrders(String query);
    Page<OrderDto> getAllOrders(Pageable pageable);
}
