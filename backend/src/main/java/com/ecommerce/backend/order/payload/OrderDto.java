package com.ecommerce.backend.order.payload;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing an entire order.
 * This is used for transferring order details between service layers and controllers.
 */
public record OrderDto(
        Long id,
        Long userId,
        Instant orderDate,
        String status, // e.g., PENDING, SHIPPED, DELIVERED
        BigDecimal totalAmount,
        String shippingAddress,
        List<OrderItemDto> items
) {}
