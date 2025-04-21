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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

        double unitPrice = product.getProduct_price();
        int quantity = dto.getQuantity();
        double subTotal = unitPrice * quantity;

        int currentStock = product.getStock_quantity();
        if(quantity > currentStock) {
            throw new RuntimeException("Not enough stock! Available: " + currentStock);
        }

        product.setStock_quantity(currentStock - quantity);
        productRepo.save(product);

        OrderDetails orderDetails = orderDetailsMapper.orderDetailsDtoToOrderDetails(dto);
        orderDetails.setOrders(orders);
        orderDetails.setProduct(product);
        orderDetails.setSubtotal(subTotal);

        OrderDetails saved = orderDetailsRepo.save(orderDetails);

        List<OrderDetails> allDetails = orderDetailsRepo.findByOrders(orders);

        double totalOrderPrice = allDetails.stream().mapToDouble(OrderDetails::getSubtotal).sum();
        orders.setOrder_price(totalOrderPrice);
        orderRepo.save(orders);
        return orderDetailsMapper.orderDetailsToOrderDetailsDto(saved);
    }

    @Override
    public List<OrderDetailsDto> searchOrderDetails(String query) {
        List<OrderDetails> orderDetails = orderDetailsRepo.searchOrderDetails(query);
        return orderDetails
                .stream()
                .map(orderDetailsMapper::orderDetailsToOrderDetailsDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderDetails(Integer id) {

        OrderDetails existing = orderDetailsRepo.findById(id).orElseThrow(()-> new RuntimeException("Order Detail not found with id: " + id));

        Orders orders = existing.getOrders();

        Product p2 = existing.getProduct();
        int currentStock = p2.getStock_quantity();
        p2.setStock_quantity(currentStock + existing.getQuantity());
        productRepo.save(p2);

        orderDetailsRepo.deleteById(id);

        List<OrderDetails> remainingDetails = orderDetailsRepo.findByOrders(orders);
        double total = remainingDetails.stream().mapToDouble(OrderDetails::getSubtotal).sum();

        orders.setOrder_price(total);
        orderRepo.save(orders);
    }

    @Override
    public OrderDetailsDto updateOrderDetails(Integer id, OrderDetailsDto dto) {
        OrderDetails existing = orderDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetails not found with id: " + id));

        existing.setQuantity(dto.getQuantity());

        Product p1 = existing.getProduct();
        double unitPrice = p1.getProduct_price();
        int quantity = dto.getQuantity();
        double subTotal = unitPrice * quantity;
        existing.setSubtotal(subTotal);

        int previousStock = p1.getStock_quantity();
        int newStock = dto.getQuantity();
        int difference = newStock - previousStock;

        Integer currentStock = p1.getStock_quantity();
        if(difference > 0 && currentStock > difference) {
            throw new RuntimeException("Not enough stock! Available: " + currentStock);
        }

        p1.setStock_quantity(currentStock - difference);
        productRepo.save(p1);

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
        OrderDetails saved = orderDetailsRepo.save(existing);

        Orders order = saved.getOrders();
        List<OrderDetails> allDetailsForOrder = orderDetailsRepo.findByOrders(order);
        double totalOrderPrice = allDetailsForOrder.stream()
                .mapToDouble(OrderDetails::getSubtotal)
                .sum();
        order.setOrder_price(totalOrderPrice);
        orderRepo.save(order);

        return orderDetailsMapper.orderDetailsToOrderDetailsDto(orderDetailsRepo.save(existing));
    }

    @Override
    public Page<OrderDetailsDto> getAllOrderDetails(Pageable pageable) {
        return orderDetailsRepo.findAll(pageable)
                .map(orderDetailsMapper::orderDetailsToOrderDetailsDto);
    }

}
