package com.ecommerce.backend.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for requesting to add or update an item in a cart.
 * This ensures the controller receives structured and validated input.
 */
public class CartItemRequestDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // --- Constructors, Getters, and Setters ---

    public CartItemRequestDto() {
    }

    public CartItemRequestDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Optional: Override toString for easy logging/debugging
     */
    @Override
    public String toString() {
        return "CartItemRequestDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
