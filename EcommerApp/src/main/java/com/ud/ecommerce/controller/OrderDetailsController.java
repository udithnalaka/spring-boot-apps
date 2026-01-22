package com.ud.ecommerce.controller;

import com.ud.ecommerce.dto.OrderDetailDto;
import com.ud.ecommerce.facade.OrderAsyncFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class OrderDetailsController {

    @Autowired
    private OrderAsyncFacade asynFacade;

    @GetMapping("/{productId}")
    public ResponseEntity<OrderDetailDto> getOrderDetails(@PathVariable Long productId) {

        return ResponseEntity.ok(asynFacade.getOrderDetails(productId));
    }
}
