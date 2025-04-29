package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface OrderService {

    OrderDto saveOrder(OrderDto orderDto);
    OrderDto updateOrder(OrderDto orderDto);
    OrderDto deleteOrder(Integer orderId);
    List<OrderDto> searchOrders(String query);
    Page<OrderDto> getAllOrders(Pageable pageable);
}
