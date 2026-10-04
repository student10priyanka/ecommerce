package com.MAF.ecommerce.event;

public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private double totalAmount;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId, Long userId, double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}