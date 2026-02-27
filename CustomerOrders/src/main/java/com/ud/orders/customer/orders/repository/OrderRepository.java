package com.ud.orders.customer.orders.repository;

import com.ud.orders.customer.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);

    List<Order> findByStatus(String status);
}

