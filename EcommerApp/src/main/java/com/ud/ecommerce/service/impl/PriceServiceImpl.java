package com.ud.ecommerce.service.impl;

import com.ud.ecommerce.entity.Price;
import com.ud.ecommerce.repository.PriceRepository;
import com.ud.ecommerce.service.PriceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ud.ecommerce.helper.OrderHelper.addDelay;

@Slf4j
@AllArgsConstructor
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Override
    public Price getPriceByProductId(Long productId) {
        log.info("Service call to fetch Price. product id: {}", productId);
        addDelay();
        return priceRepository.findByProductId(productId).orElse(null);
    }

}
