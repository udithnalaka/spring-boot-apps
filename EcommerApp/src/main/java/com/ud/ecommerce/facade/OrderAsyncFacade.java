package com.ud.ecommerce.facade;

import com.ud.ecommerce.dto.OrderDetailDto;
import com.ud.ecommerce.entity.Inventory;
import com.ud.ecommerce.entity.Price;
import com.ud.ecommerce.entity.Product;
import com.ud.ecommerce.service.InventoryService;
import com.ud.ecommerce.service.PriceService;
import com.ud.ecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderAsyncFacade {

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PriceService priceService;

    private CompletableFuture<Product> getProductById (long productId) {
        return CompletableFuture.supplyAsync(() -> productService.findById(productId));
    }

    private CompletableFuture<Inventory> getInventoryByProductId( long productId) {
        return CompletableFuture.supplyAsync(() -> inventoryService.getInventoryByProductId(productId));
    }

    private CompletableFuture<Price> getPriceByProductId( long productId) {
        return CompletableFuture.supplyAsync(() -> priceService.getPriceByProductId(productId));
    }

    public OrderDetailDto getOrderDetails(long productId) {
        //get Product, Price and Inventory details Asynchronously
        CompletableFuture<Product> productFuture = getProductById(productId);
        CompletableFuture<Inventory> inventoryFuture = getInventoryByProductId(productId);
        CompletableFuture<Price> priceFuture = getPriceByProductId(productId);

        //wait for all futures to finish.
        CompletableFuture.allOf(productFuture, inventoryFuture, priceFuture);

        // get the result after request completed
        Product product = productFuture.join();
        Inventory inventory = inventoryFuture.join();
        Price price = priceFuture.join();

        log.info("Received Product details: {}, Received Price details: {}, Received Inventory details: {} ",
                product, price, inventory);

        //build and return
        return OrderDetailDto.builder()
                .id(productId)
                .name(product.getName())
                .description(product.getDescription())
                .price(price.getPrice())
                .availableQuantity(inventory.getAvailableQuantity())
                .status(inventory.getStatus())
                .build();
    }
}
