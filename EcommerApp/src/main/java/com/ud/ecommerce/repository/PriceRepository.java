package com.ud.ecommerce.repository;

import com.ud.ecommerce.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findByProductId(Long productId);
}
