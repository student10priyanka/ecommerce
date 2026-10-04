package com.MAF.ecommerce.repository;

import com.MAF.ecommerce.model.Cart;
import com.MAF.ecommerce.model.CartItem;
import com.MAF.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}