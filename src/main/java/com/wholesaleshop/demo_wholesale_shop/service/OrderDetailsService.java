package com.wholesaleshop.demo_wholesale_shop.service;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderDetailsService {

    OrderDetailsDto saveOrderDetails(OrderDetailsDto orderDetailsDto);
    List<OrderDetailsDto> searchOrderDetails(String query);
    void deleteOrderDetails(Integer id);
    Page<OrderDetailsDto> getAllOrderDetails(Pageable pageable);
    OrderDetailsDto updateOrderDetails(Integer id, OrderDetailsDto orderDetailsDto);
}
