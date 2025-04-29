package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.InvoiceDto;
import com.wholesaleshop.demo_wholesale_shop.dto.OrderDetailsDto;
import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.dto.PaymentDto;
import com.wholesaleshop.demo_wholesale_shop.entity.*;
import com.wholesaleshop.demo_wholesale_shop.exception.OutOfStockException;
import com.wholesaleshop.demo_wholesale_shop.exception.ResourceNotFoundException;
import com.wholesaleshop.demo_wholesale_shop.repo.*;
import com.wholesaleshop.demo_wholesale_shop.service.OrderService;
import com.wholesaleshop.demo_wholesale_shop.utils.InvoiceMapper;
import com.wholesaleshop.demo_wholesale_shop.utils.OrderDetailsMapper;
import com.wholesaleshop.demo_wholesale_shop.utils.OrderMapper;
import com.wholesaleshop.demo_wholesale_shop.utils.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private PaymentMapper paymentMapper;



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
        Orders order = new Orders();
        order.setOrderDate(orderDto.getOrderDate());

        Customer customer = customerRepo.findById(orderDto.getCustomer_id())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomer_id()));
        order.setCustomer(customer);

        double totalAmount = 0.0;

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        List<OrderDetailsDto> orderDetailsDto = new ArrayList<>();

        for (OrderDetailsDto detailsDto : orderDto.getOrderDetails()) {
            OrderDetails detail = new OrderDetails();

            // check product exists and retrieve the product and set it
            Product isProductExist = productRepo.findById(detailsDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + detailsDto.getProductId()));
            detail.setProduct(isProductExist); // Set the product

            //check stock count availability
            if (isProductExist.getStock_quantity() < detailsDto.getQuantity()) {
                throw  new OutOfStockException("Stock has only " + detailsDto.getQuantity() +
                        " items from product " + detailsDto.getProductId() + ".");
            }

            //deduct ordered items qty from product stock
            isProductExist.setStock_quantity(isProductExist.getStock_quantity() - detailsDto.getQuantity());
            productRepo.save(isProductExist);

            // Set values from DTO
            detail.setQuantity(detailsDto.getQuantity());
            double unitPrice = isProductExist.getProduct_price(); // Get from product entity
            detail.setUnitPrice(unitPrice);

            // Calculate subtotal
            double subtotal = detailsDto.getQuantity() * unitPrice; // Use fetched price
            detail.setSubtotal(subtotal);

            totalAmount += subtotal;

            detail.setOrders(order); // this is associate with order
            orderDetailsList.add(detail);

        }

        // Set total to order
        order.setOrder_price(totalAmount);

        // Save order first
        Orders savedOrder = orderRepo.save(order);

        // Save order items
        List<OrderDetails> savedOrderItems = orderDetailsRepo.saveAll(orderDetailsList);

        // Prepare dtos for order items
        for (OrderDetails savedItem : savedOrderItems) {

            OrderDetailsDto savedItemDTO = orderDetailsMapper.orderDetailsToOrderDetailsDto(savedItem);
            orderDetailsDto.add(savedItemDTO);
        }

        // Save invoice
        Invoice invoice = new Invoice();
        invoice.setInvoice_date(orderDto.getOrderDate());
        invoice.setOrders(savedOrder);
        invoice.setInvoice_amount(totalAmount);
        invoiceRepo.save(invoice);

        // Save payment
        Payment payment = new Payment();
        payment.setPayment_date(orderDto.getOrderDate());
        payment.setPaid_amount(orderDto.getPayment().getPaid_amount());
        payment.setPayment_method(orderDto.getPayment().getPayment_method());
        payment.setOrders(savedOrder);
        payment.setInvoice(invoice);
        paymentRepo.save(payment);

        OrderDto savedOrderDTO = orderMapper.orderToOrderDto(savedOrder);
        savedOrderDTO.setOrderDetails(orderDetailsDto);

        InvoiceDto invoiceDTO = invoiceMapper.invoiceToInvoiceDto(invoice);
        savedOrderDTO.setInvoice(invoiceDTO);

        PaymentDto paymentDTO = paymentMapper.paymentToPaymentDto(payment);
        savedOrderDTO.setPayment(paymentDTO);

        return savedOrderDTO;
    }

    /**
     * Update an existing order.
     * - Updates order details based on the provided OrderDto.
     * - Associates the updated order with the specified customer.
     *
     * @param orderDto the OrderDto with updated data
     * @return the updated OrderDto, or null if the order was not found
     */
    @Transactional
    @Override
    public OrderDto updateOrder(OrderDto orderDto) throws ResourceNotFoundException {

        // Find the existing order
        Orders existingOrder = orderRepo.findById(orderDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderDto.getOrderId()));

        // Update order date
        existingOrder.setOrderDate(orderDto.getOrderDate());

        // Update customer if needed
        if (orderDto.getCustomer_id() != null) {
            Customer customer = customerRepo.findById(orderDto.getCustomer_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDto.getCustomer_id()));
            existingOrder.setCustomer(customer);
        }

        // Reset the total amount
        double totalAmount = 0.0;

        // Delete existing order items first and restore stock
        List<OrderDetails> existingDetails = orderDetailsRepo.findByOrders(existingOrder);
        for (OrderDetails details : existingDetails) {
            Product product = details.getProduct();
            product.setStock_quantity(product.getStock_quantity() + details.getQuantity()); // Restore stock
            productRepo.save(product);
        }
        orderDetailsRepo.deleteAll(existingDetails);

        // Prepare new order items
        List<OrderDetails> newOrderDetails = new ArrayList<>();
        List<OrderDetailsDto> orderDetailsDto = new ArrayList<>();

        for (OrderDetailsDto detailsDto : orderDto.getOrderDetails()) {
            OrderDetails newDetail = new OrderDetails();

            // Fetch product
            Product product = productRepo.findById(detailsDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + detailsDto.getProductId()));

            // Check stock
            if (product.getStock_quantity() < detailsDto.getQuantity()) {
                throw new OutOfStockException("Stock has only " + product.getStock_quantity() + " items from product " + detailsDto.getProductId() + ".");
            }

            // Deduct qty from stock
            product.setStock_quantity(product.getStock_quantity() - detailsDto.getQuantity());
            productRepo.save(product);

            // Set item details using product price
            double unitPrice = product.getProduct_price(); //get unit price from product entity
            newDetail.setProduct(product);
            newDetail.setQuantity(detailsDto.getQuantity());
            newDetail.setUnitPrice(unitPrice);
            double subtotal = detailsDto.getQuantity() * unitPrice;
            newDetail.setSubtotal(subtotal);

            totalAmount += subtotal;
            newDetail.setOrders(existingOrder);
            newOrderDetails.add(newDetail);
        }

        // Set total to order
        existingOrder.setOrder_price(totalAmount);

        // Save updated order
        Orders updatedOrder = orderRepo.save(existingOrder);

        // Save new order items
        List<OrderDetails> savedDetails = orderDetailsRepo.saveAll(newOrderDetails);

        // Map saved items to DTO
        for (OrderDetails savedDetail : savedDetails) {
            OrderDetailsDto savedDetailsDto = orderDetailsMapper.orderDetailsToOrderDetailsDto(savedDetail);
            orderDetailsDto.add(savedDetailsDto);
        }

        // Update invoice
        Invoice invoice = invoiceRepo.findByOrders(updatedOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for order id: " + orderDto.getInvoice().getInvoice_id()));
        invoice.setInvoice_date(orderDto.getOrderDate());
        invoice.setInvoice_amount(totalAmount);
        invoiceRepo.save(invoice);

        // Update payment
        Payment payment = paymentRepo.findByOrders(updatedOrder)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderDto.getPayment().getPayment_id()));
        payment.setPayment_date(orderDto.getOrderDate());
        payment.setPaid_amount(orderDto.getPayment().getPaid_amount());
        payment.setPayment_method(orderDto.getPayment().getPayment_method());
        payment.setInvoice(invoice);
        paymentRepo.save(payment);

        // Prepare return DTO
        OrderDto updatedOrderDTO = orderMapper.orderToOrderDto(updatedOrder);
        updatedOrderDTO.setOrderDetails(orderDetailsDto);
        updatedOrderDTO.setInvoice(invoiceMapper.invoiceToInvoiceDto(invoice));
        updatedOrderDTO.setPayment(paymentMapper.paymentToPaymentDto(payment));

        return updatedOrderDTO;
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
