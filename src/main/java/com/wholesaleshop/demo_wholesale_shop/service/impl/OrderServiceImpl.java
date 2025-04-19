package com.wholesaleshop.demo_wholesale_shop.service.impl;

import com.wholesaleshop.demo_wholesale_shop.dto.OrderDto;
import com.wholesaleshop.demo_wholesale_shop.entity.Customer;
import com.wholesaleshop.demo_wholesale_shop.entity.Orders;
import com.wholesaleshop.demo_wholesale_shop.repo.CustomerRepo;
import com.wholesaleshop.demo_wholesale_shop.repo.OrderRepo;
import com.wholesaleshop.demo_wholesale_shop.service.OrderService;
import com.wholesaleshop.demo_wholesale_shop.utils.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        Orders orders = orderMapper.orderDtoToOrder(orderDto);

        // Set Customer using customer_id from DTO
        Optional<Customer> customerOpt = customerRepo.findById(orderDto.getCustomer_id());
        customerOpt.ifPresent(orders::setCustomer);

        Orders saved = orderRepo.save(orders);
        return orderMapper.orderToOrderDto(saved);
    }

    @Override
    public OrderDto updateOrder(Integer orderId,OrderDto orderDto) {
        Optional<Orders> existingOpt = orderRepo.findById(orderDto.getOrder_id());
        if (existingOpt.isPresent()) {
            Orders existing = existingOpt.get();
            existing.setOrderDate(orderDto.getOrder_date());
            existing.setOrder_price(orderDto.getOrder_price());

            // Optionally update the customer too
            Optional<Customer> customerOpt = customerRepo.findById(orderDto.getCustomer_id());
            customerOpt.ifPresent(existing::setCustomer);

            Orders updated = orderRepo.save(existing);
            return orderMapper.orderToOrderDto(updated);
        }
        return null; // handle not-found case in controller
    }

    @Override
    public OrderDto deleteOrder(Integer orderId) {
        Optional<Orders> opt = orderRepo.findById(orderId);
        if (opt.isPresent()) {
            Orders entity = opt.get();
            orderRepo.deleteById(orderId);
            return orderMapper.orderToOrderDto(entity);
        }
        return null; // handle not-found case in controller
    }

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        Page<Orders> page = orderRepo.findAll(pageable);
        return page.map(orderMapper::orderToOrderDto);
    }

}
