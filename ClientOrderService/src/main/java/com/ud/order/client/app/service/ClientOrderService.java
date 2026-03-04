package com.ud.order.client.app.service;

import com.ud.order.client.app.dto.OrderDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ClientOrderService {

    public CompletableFuture<List<OrderDto>> getOrderForCustomer();
}
