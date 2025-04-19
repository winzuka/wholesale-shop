package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.entity.OrderDetails;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderDetailsRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.ProductRepo;
import com.wholesaleshop.demo_wholesale_shop.service.OrderDetailsService;
import com.wholesaleshop.demo_wholesale_shop.utils.OrderDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    OrderDetailsRepo orderDetailsRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderDetailsMapper orderDetailsMapper;

    @Override
    public OrderDetailsDto saveOrderDetails(OrderDetailsDto dto) {
        Orders orders = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));

        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        OrderDetails orderDetails = orderDetailsMapper.orderDetailsDtoToOrderDetails(dto);
        orderDetails.setOrders(orders);
        orderDetails.setProduct(product);

        return orderDetailsMapper.orderDetailsToOrderDetailsDto(orderDetailsRepo.save(orderDetails));
    }

    @Override
    public OrderDetailsDto getOrderDetailsById(Integer id) {
        OrderDetails od = orderDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetails not found with id: " + id));
        return orderDetailsMapper.orderDetailsToOrderDetailsDto(od);
    }

    @Override
    public List<OrderDetailsDto> getAllOrderDetails() {
        return orderDetailsRepo.findAll()
                .stream()
                .map(orderDetailsMapper::orderDetailsToOrderDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderDetails(Integer id) {
        orderDetailsRepo.deleteById(id);
    }

    @Override
    public OrderDetailsDto updateOrderDetails(Integer id, OrderDetailsDto dto) {
        OrderDetails existing = orderDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetails not found with id: " + id));

        existing.setQuantity(dto.getQuantity());
        existing.setSubtotal(dto.getSubtotal());

        // Update order and product if needed
        if (!existing.getOrders().getOrderId().equals(dto.getOrderId())) {
            Orders orders = orderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));
            existing.setOrders(orders);
        }

        if (!existing.getProduct().getProductId().equals(dto.getProductId())) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));
            existing.setProduct(product);
        }

        return orderDetailsMapper.orderDetailsToOrderDetailsDto(orderDetailsRepo.save(existing));
    }
}
