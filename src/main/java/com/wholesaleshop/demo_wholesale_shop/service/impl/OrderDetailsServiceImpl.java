package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.entity.OrderDetails;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.entity.Product;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
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
    OrderDetailsRepo orderDetailsRepo; // Repository for interacting with OrderDetails data

    @Autowired
    OrderRepo orderRepo; // Repository for interacting with Order data

    @Autowired
    ProductRepo productRepo; // Repository for interacting with Product data

    @Autowired
    OrderDetailsMapper orderDetailsMapper; // Mapper to convert between OrderDetails DTO and entity

    /**
     * Save order details for a specific order and product.
     * - Validates if the order and product exist.
     * - Checks stock availability before saving order details.
     * - Updates the product's stock and recalculates the order price.
     *
     * @param dto the OrderDetails DTO to save
     * @return the saved OrderDetails DTO
     */
    @Override
    public OrderDetailsDto saveOrderDetails(OrderDetailsDto dto) {
        // Fetch the order and validate its existence
        Orders orders = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId()));

        // Fetch the product and validate its existence
        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));

        // Calculate subtotal based on unit price and quantity
        double unitPrice = product.getProduct_price();
        int quantity = dto.getQuantity();
        double subTotal = unitPrice * quantity;

        // Check if there's enough stock
        int currentStock = product.getStock_quantity();
        if(quantity > currentStock) {
            throw new RuntimeException("Not enough stock! Available: " + currentStock);
        }

        // Update product stock
        product.setStock_quantity(currentStock - quantity);
        productRepo.save(product);

        // Map DTO to entity and set relationships
        OrderDetails orderDetails = orderDetailsMapper.orderDetailsDtoToOrderDetails(dto);
        orderDetails.setOrders(orders);
        orderDetails.setProduct(product);
        orderDetails.setSubtotal(subTotal);

        // Save order details and update order price
        OrderDetails saved = orderDetailsRepo.save(orderDetails);
        List<OrderDetails> allDetails = orderDetailsRepo.findByOrders(orders);

        // Recalculate total order price and save
        double totalOrderPrice = allDetails.stream().mapToDouble(OrderDetails::getSubtotal).sum();
        orders.setOrder_price(totalOrderPrice);
        orderRepo.save(orders);
        return orderDetailsMapper.orderDetailsToOrderDetailsDto(saved);
    }

    /**
     * Search for order details based on a query.
     *
     * @param query the search query
     * @return a list of OrderDetails DTOs that match the query
     */
    @Override
    public List<OrderDetailsDto> searchOrderDetails(String query) {
        // Fetch order details matching the search query
        List<OrderDetails> orderDetails = orderDetailsRepo.searchOrderDetails(query);
        return orderDetails
                .stream()
                .map(orderDetailsMapper::orderDetailsToOrderDetailsDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete an order detail by ID.
     * - Restores the stock of the product involved.
     * - Recalculates the total order price after deletion.
     *
     * @param id the ID of the OrderDetails entity to delete
     */
    @Override
    public void deleteOrderDetails(Integer id) {
        // Fetch the existing order details
        OrderDetails existing = orderDetailsRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order Detail not found with id: " + id));

        // Get the order and product involved in this order detail
        Orders orders = existing.getOrders();
        Product p2 = existing.getProduct();

        // Restore product stock after deletion
        int currentStock = p2.getStock_quantity();
        p2.setStock_quantity(currentStock + existing.getQuantity());
        productRepo.save(p2);

        // Delete the order details and update order price
        orderDetailsRepo.deleteById(id);

        List<OrderDetails> remainingDetails = orderDetailsRepo.findByOrders(orders);
        double total = remainingDetails.stream().mapToDouble(OrderDetails::getSubtotal).sum();

        orders.setOrder_price(total);
        orderRepo.save(orders);
    }

    /**
     * Update the order details for a specific order.
     * - Validates stock and updates product relationships.
     * - Recalculates the order price after modification.
     *
     * @param id the ID of the OrderDetails entity to update
     * @param dto the new OrderDetails DTO to update with
     * @return the updated OrderDetails DTO
     */
    @Override
    public OrderDetailsDto updateOrderDetails(Integer id, OrderDetailsDto dto) {
        // Fetch the existing order details
        OrderDetails existing = orderDetailsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetails not found with id: " + id));

        // Update quantity and recalculate subtotal
        existing.setQuantity(dto.getQuantity());

        Product p1 = existing.getProduct();
        double unitPrice = p1.getProduct_price();
        int quantity = dto.getQuantity();
        double subTotal = unitPrice * quantity;
        existing.setSubtotal(subTotal);

        // Ensure sufficient stock for the new quantity
        int previousStock = p1.getStock_quantity();
        int newStock = dto.getQuantity();
        int difference = newStock - previousStock;

        Integer currentStock = p1.getStock_quantity();
        if(difference > 0 && currentStock > difference) {
            throw new RuntimeException("Not enough stock! Available: " + currentStock);
        }

        // Update stock based on the difference
        p1.setStock_quantity(currentStock - difference);
        productRepo.save(p1);

        // Update order and product relationships if changed
        if (!existing.getOrders().getOrderId().equals(dto.getOrderId())) {
            Orders orders = orderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + dto.getOrderId()));
            existing.setOrders(orders);
        }

        if (!existing.getProduct().getProductId().equals(dto.getProductId())) {
            Product product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));
            existing.setProduct(product);
        }

        // Save the updated order details and recalculate order price
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

    /**
     * Retrieve all order details with pagination.
     *
     * @param pageable the pagination information
     * @return a page of OrderDetails DTOs
     */
    @Override
    public Page<OrderDetailsDto> getAllOrderDetails(Pageable pageable) {
        // Fetch paginated order details
        return orderDetailsRepo.findAll(pageable)
                .map(orderDetailsMapper::orderDetailsToOrderDetailsDto);
    }

}
