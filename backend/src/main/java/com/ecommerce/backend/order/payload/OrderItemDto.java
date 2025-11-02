package com.ecommerce.backend.order.payload;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing a single item within an order.
 * This is used inside the OrderDto to show what products were purchased.
 * Uses a record for conciseness and immutability.
 *
 * @param productId The ID of the product purchased.
 * @param quantity The number of units of this product.
 * @param price The price of the product at the time of the order.
 */
public record OrderItemDto(
        Long productId,
        Integer quantity,
        BigDecimal price
) {}
