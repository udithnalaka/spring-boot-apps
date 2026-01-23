package com.ud.ecommerce.service.impl;

import com.ud.ecommerce.entity.Product;
import com.ud.ecommerce.helper.OrderHelper;
import com.ud.ecommerce.repository.ProductRepository;
import com.ud.ecommerce.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product findById(Long id) {
        log.info("Service call to fetch Product. product id: {}", id);
        OrderHelper.addDelay();
        return productRepository.findById(id).orElse(null);
    }

}
