package com.ud.orders.customer.orders.controller;

import com.ud.orders.customer.orders.entity.Order;
import com.ud.orders.customer.orders.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Order saveOrder(@RequestBody Order order) { //TODO: best practice to use dto's for controller layer and abstract the entity objects.
        return service.create(order);
    }

    @GetMapping("/customer/{id}")
    public List<Order> getOrdersForCustomer(@PathVariable Long id) {
        return service.getOrdersForCustomer(id);
    }

    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }
}

