package com.ud.order.client.app.controller;

import com.ud.order.client.app.dto.OrderDto;
import com.ud.order.client.app.service.ClientOrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1/client/order")
public class ClientOrderController {

    private final ClientOrderServiceImpl clientOrderService;


    public ClientOrderController(ClientOrderServiceImpl clientOrderService) {
        this.clientOrderService = clientOrderService;
    }

    @GetMapping("/")
    public CompletableFuture<List<OrderDto>> getOrderForCustomer() {

        log.info("calling getOrderForCustomer to get the Orders for a given customer");
        return clientOrderService.getOrderForCustomer();
    }
}
