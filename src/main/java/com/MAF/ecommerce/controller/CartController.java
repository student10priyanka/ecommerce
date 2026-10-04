package com.MAF.ecommerce.controller;

import com.MAF.ecommerce.dto.AddToCartRequest;
import com.MAF.ecommerce.model.Cart;
import com.MAF.ecommerce.model.CartItem;
import com.MAF.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/cart/items")
    public ResponseEntity<CartItem> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {

        CartItem cartItem = cartService.addToCart(userId, request);

        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/{userId}/cart")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {

        Cart cart = cartService.getCart(userId);

        return ResponseEntity.ok(cart);
    }
}