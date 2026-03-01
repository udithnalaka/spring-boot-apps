package com.ud.orders.customer.orders.controller;

import com.ud.orders.customer.orders.entity.Order;
import com.ud.orders.customer.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Customer Orders", description = "APIs for managing orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @Operation(summary = "create an Order", description = "save Order details to database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order saved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping
    public Order saveOrder(@RequestBody Order order) { //TODO: best practice to use dto's for controller layer and abstract the entity objects.
        return service.create(order);
    }

    @Operation(summary = "Get Order by Customer Id", description = "Retrieve order details by customer id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Orders not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/customer/{id}")
    public List<Order> getOrdersForCustomer(@PathVariable Long id) {
        return service.getOrdersForCustomer(id);
    }

    @Operation(summary = "Get Order by Status", description = "Retrieve order details by order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
            @ApiResponse(responseCode = "404", description = "Orders not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }
}

