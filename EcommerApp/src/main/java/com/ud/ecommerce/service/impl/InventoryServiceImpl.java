package com.ud.ecommerce.service.impl;

import com.ud.ecommerce.entity.Inventory;
import com.ud.ecommerce.repository.InventoryRepository;
import com.ud.ecommerce.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ud.ecommerce.helper.OrderHelper.addDelay;

@Slf4j
@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory getInventoryByProductId(Long productId) {
        log.info("Service call to fetch product id: {}", productId);
        addDelay();
        return inventoryRepository.findByProductId(productId).orElse(null);
    }

}
