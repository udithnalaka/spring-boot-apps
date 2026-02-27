package com.ud.orders.customer.orders.service;

import com.ud.orders.customer.orders.entity.Order;
import com.ud.orders.customer.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
    }

    public List<Order> getByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
