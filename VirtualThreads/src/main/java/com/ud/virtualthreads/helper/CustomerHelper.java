package com.ud.virtualthreads.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ud.virtualthreads.dto.Customer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class CustomerHelper {

    private final ObjectMapper objectMapper;
    private List<Customer> customers;

    public CustomerHelper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    @PostConstruct
    public void createCustomers() {
        try {
            // Read the file as an InputStream from the classpath
            InputStream inputStream = new ClassPathResource("customer.json").getInputStream();

            // Map the JSON array to a List of Customer objects using TypeReference
            customers = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            log.info("Loaded Customers: {}", customers.size());
        } catch (IOException e) {
            // Handle the exception appropriately
            throw new RuntimeException("Failed to load customers from JSON file", e);
        }
    }

    public void generateCustomerReport() {
        log.info("generating Customer report. Thread name: {}", Thread.currentThread());

        try {
            CSVReportUtil.writeCustomersToCSV(customers, "csv_customer_report.csv");
        } catch (Exception e) {
            log.info("Error creating CSV Customer report");
            throw new RuntimeException(e);
        }
    }
}
