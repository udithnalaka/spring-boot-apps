package com.ud.order.client.app.service;

import com.ud.order.client.app.dto.OrderDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private final RestClient orderServiceClient;

    public ClientOrderServiceImpl(RestClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    /**
     * <pre>Get Orders for a given customer.
     * a Rest call will be made to the Customer Orders service to get the details.
     * Note: make sure the <b>CustomerOrders</b> Service is running locally in port 8080.
     *
     * @return List<OrderDto>  of Orders for the customer
     * </pre>
     */
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallBack")
    @Retry(name = "order-service")
    @TimeLimiter(name = "order-service")
    public CompletableFuture<List<OrderDto>> getOrderForCustomer() {

        long customerId = 1;

        return CompletableFuture.supplyAsync(() ->
                orderServiceClient.get()
                        .uri("customer/".concat(String.valueOf(customerId)))
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<OrderDto>>() {}));

        /*return orderServiceClient.get()
                .uri("customer/".concat(String.valueOf(customerId)))
                .retrieve()
                .body(new ParameterizedTypeReference<List<OrderDto>>() {});*/
    }

    public String fallBack(Throwable ex) {
        return "Resilience4j: Fallback response: Order Service unavailable in port 8080.";
    }
}
