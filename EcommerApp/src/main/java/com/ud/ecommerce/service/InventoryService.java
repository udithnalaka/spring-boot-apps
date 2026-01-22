package com.ud.ecommerce.service;

import com.ud.ecommerce.entity.Inventory;

public interface InventoryService {

    Inventory getInventoryByProductId(Long productId);
}
