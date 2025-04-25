package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
import com.wholesaleshop.demo_wholesale_shop.repo.CustomerRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.service.OrderService;
import com.wholesaleshop.demo_wholesale_shop.utils.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo; // Repository for interacting with Orders data

    @Autowired
    private CustomerRepo customerRepo; // Repository for interacting with Customer data

    @Autowired
    private OrderMapper orderMapper; // Mapper to convert between OrderDto and Orders entity

    /**
     * Save a new order.
     * - Maps the OrderDto to an Order entity.
     * - Associates the order with the specified customer.
     *
     * @param orderDto the OrderDto to save
     * @return the saved OrderDto
     */
    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        // Map the OrderDto to the Orders entity
        Orders orders = orderMapper.orderDtoToOrder(orderDto);

        // Fetch the customer by ID and associate with the order
        Optional<Customer> customerOpt = customerRepo.findById(orderDto.getCustomer_id());
        customerOpt.ifPresent(orders::setCustomer);

        // Save the order and return the saved OrderDto
        Orders saved = orderRepo.save(orders);
        return orderMapper.orderToOrderDto(saved);
    }

    /**
     * Update an existing order.
     * - Updates order details based on the provided OrderDto.
     * - Associates the updated order with the specified customer.
     *
     * @param orderDto the OrderDto with updated data
     * @return the updated OrderDto, or null if the order was not found
     */
    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        // Fetch the existing order by ID
        Orders existingOrder = orderRepo.findById(orderDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderDto.getOrderId()));


            // Update order properties
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setOrder_price(orderDto.getOrder_price());

            // Fetch the customer by ID and associate with the order
            Customer existingCustomer = customerRepo.findById(orderDto.getCustomer_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + orderDto.getCustomer_id()));

            // Save the updated order and return the updated OrderDto
            Orders updated = orderRepo.save(existingOrder);
            return orderMapper.orderToOrderDto(updated);
    }

    /**
     * Delete an order by its ID.
     * - Fetches the order, deletes it, and returns the deleted order as OrderDto.
     *
     * @param orderId the ID of the order to delete
     * @return the deleted OrderDto, or null if the order was not found
     */
    @Override
    public OrderDto deleteOrder(Integer orderId) {
        // Fetch the order by ID
        Orders existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

            // Delete the order from the repository
            orderRepo.deleteById(orderId);

            // Return the deleted order as OrderDto
            return orderMapper.orderToOrderDto(existingOrder);
    }

    /**
     * Get all orders with pagination.
     *
     * @param pageable the pagination information
     * @return a page of OrderDtos
     */
    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        // Fetch all orders with pagination
        Page<Orders> page = orderRepo.findAll(pageable);

        // Convert Orders entities to OrderDtos
        return page.map(orderMapper::orderToOrderDto);
    }

    /**
     * Search for orders based on a query string.
     *
     * @param query the search query
     * @return a list of matching OrderDtos
     */
    @Override
    public List<OrderDto> searchOrders(String query) {
        // Fetch orders that match the search query
        List<Orders> orders = orderRepo.searchOrders(query);

        // Convert the matching orders to OrderDtos
        return orders.stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList());
    }

}
