package com.ecommerce.backend.model;

/**
 * Defines the possible statuses for an order.
 */
public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
