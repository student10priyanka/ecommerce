package com.MAF.ecommerce.service;

import com.MAF.ecommerce.dto.AddToCartRequest;
import com.MAF.ecommerce.model.Cart;
import com.MAF.ecommerce.model.CartItem;
import com.MAF.ecommerce.model.Product;
import com.MAF.ecommerce.model.User;
import com.MAF.ecommerce.repository.CartItemRepository;
import com.MAF.ecommerce.repository.CartRepository;
import com.MAF.ecommerce.repository.ProductRepository;
import com.MAF.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartItem addToCart(Long userId, AddToCartRequest request) {

        // 1. Check user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Check quantity is valid
        if (request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        // 4. Check sufficient stock
        if (request.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        // 5. Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 6. Check if product already exists in cart
        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {

            // Product already exists → increase quantity
            int newQuantity =
                    cartItem.getQuantity() + request.getQuantity();

            // Check stock again
            if (newQuantity > product.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            cartItem.setQuantity(newQuantity);

        } else {

            // Product doesn't exist → create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
        }


        return cartItemRepository.save(cartItem);
    }
    public Cart getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}