package com.MAF.ecommerce.controller;

import com.MAF.ecommerce.model.Order;
import com.MAF.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<Order> placeOrder(
            @PathVariable Long userId) {

        Order order = orderService.placeOrder(userId);

        return ResponseEntity.ok(order);
    }
}