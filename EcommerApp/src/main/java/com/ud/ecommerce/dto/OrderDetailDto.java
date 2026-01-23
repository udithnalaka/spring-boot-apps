package com.ud.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailDto {

    private Long id;

    private String name;

    private String description;

    private int availableQuantity;

    private double price;

    private String status;
}
