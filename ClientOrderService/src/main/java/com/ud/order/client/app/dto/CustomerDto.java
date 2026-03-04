package com.ud.order.client.app.dto;

import jakarta.validation.constraints.NotNull;

public record CustomerDto(
        Long id,
        @NotNull String email,
        @NotNull String name) {
}
