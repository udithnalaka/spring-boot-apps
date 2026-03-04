package com.ud.order.client.app.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        CustomerDto customer,
        LocalDateTime orderDate,
        @NotNull BigDecimal amount,
        String status) {
}
