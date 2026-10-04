package com.MAF.ecommerce.repository;

import com.MAF.ecommerce.model.Cart;
import com.MAF.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}