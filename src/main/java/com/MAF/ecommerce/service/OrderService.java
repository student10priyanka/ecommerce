package com.MAF.ecommerce.service;

import com.MAF.ecommerce.event.OrderCreatedEvent;
import com.MAF.ecommerce.kafka.OrderEventPublisher;
import com.MAF.ecommerce.model.*;
import com.MAF.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    private final OrderEventPublisher orderEventPublisher;

    public OrderService(
            UserRepository userRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            OrderEventPublisher orderEventPublisher) {

        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    public Order placeOrder(Long userId) {

        // 1. Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 3. Check cart is not empty
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 4. Create order
        Order order = new Order();
        order.setUser(user);

        double totalAmount = 0;

        // 5. Process every cart item
        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            // Check stock
            if (cartItem.getQuantity() > product.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for " + product.getName()
                );
            }

            // Calculate amount
            double itemTotal =
                    product.getPrice() * cartItem.getQuantity();

            totalAmount += itemTotal;

            // Create OrderItem
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
            orderItemRepository.save(orderItem);

            // Reduce inventory
            product.setQuantity(
                    product.getQuantity() - cartItem.getQuantity()
            );

            productRepository.save(product);
        }

        // 6. Set final amount
        order.setTotalAmount(totalAmount);

        // 7. Save order
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                user.getId(),
                savedOrder.getTotalAmount()
        );

        orderEventPublisher.publishOrderCreated(event);

        // 8. Clear cart
        cart.getItems().clear();

        cartRepository.save(cart);

        return savedOrder;
    }
}