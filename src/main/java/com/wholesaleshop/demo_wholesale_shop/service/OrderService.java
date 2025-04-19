package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto);

    OrderDto updateOrder(Integer orderId, OrderDto orderDto);

    OrderDto deleteOrder(Integer orderId);

    Page<OrderDto> getAllOrders(Pageable pageable);
}
