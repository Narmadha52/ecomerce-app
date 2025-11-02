package com.ecommerce.backend.cart.service;

import com.ecommerce.backend.cart.model.Cart;

import java.math.BigDecimal;

/**
 * Defines the contract for managing shopping cart operations.
 */
public interface CartService {
    /**
     * Retrieves the active cart for a specific user, creating a new one if none exists.
     * @param userId The ID of the user.
     * @return The user's Cart.
     */
    Cart getCartByUserId(Long userId);

    /**
     * Adds a product to the cart or updates the quantity if it already exists.
     * @param userId The ID of the user.
     * @param productId The ID of the product to add.
     * @param quantity The quantity to add.
     * @return The updated Cart.
     */
    Cart addToCart(Long userId, Long productId, int quantity);

    /**
     * Updates the quantity of a specific cart item. If quantity is 0 or less, removes the item.
     * @param userId The ID of the user.
     * @param productId The ID of the product (cart item) to update.
     * @param quantity The new quantity.
     * @return The updated Cart.
     */
    Cart updateCartItemQuantity(Long userId, Long productId, int quantity);

    /**
     * Removes a specific product item from the cart.
     * @param userId The ID of the user.
     * @param productId The ID of the product (cart item) to remove.
     */
    void removeCartItem(Long userId, Long productId);

    /**
     * Empties all items from the user's cart.
     * @param userId The ID of the user.
     */
    void clearCart(Long userId);

    /**
     * Calculates the total monetary value of all items in the user's cart.
     * @param userId The ID of the user.
     * @return The calculated total amount.
     */
    BigDecimal calculateTotal(Long userId);
}
