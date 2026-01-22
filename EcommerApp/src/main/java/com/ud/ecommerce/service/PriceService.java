package com.ud.ecommerce.service;

import com.ud.ecommerce.entity.Price;

public interface PriceService {

    Price getPriceByProductId(Long productId);
}
