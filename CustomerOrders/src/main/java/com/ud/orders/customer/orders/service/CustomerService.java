package com.ud.orders.customer.orders.service;

import com.ud.orders.customer.orders.entity.Customer;
import com.ud.orders.customer.orders.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepo;

    public CustomerService(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer create(Customer c) {
        return customerRepo.save(c);
    }

    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email).orElseThrow();
    }
}

