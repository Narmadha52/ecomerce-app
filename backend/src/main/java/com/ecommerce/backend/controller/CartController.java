package com.ecommerce.backend.controller;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.service.CartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing the user's shopping cart.
 * All operations assume the user is authenticated, and the userId is extracted
 * from the security context (simulated here by getCurrentUserId()).
 */
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // --- Placeholder for a request DTO ---
    /**
     * Simple request DTO for adding items to the cart.
     */
    @Getter
    private static class CartItemRequest {
        private Long productId;
        private Integer quantity;
    }
    // ----------------------------------------

    /**
     * Simulates fetching the authenticated user's ID from Spring Security Context.
     * In a real application, this would use @AuthenticationPrincipal.
     */
    private Long getCurrentUserId() {
        // TODO: Replace with actual retrieval of authenticated user's ID
        // For development/testing, assuming a default user ID, e.g., 1L
        return 1L;
    }

    /**
     * GET /api/v1/cart
     * Retrieves the current user's shopping cart details.
     *
     * @return The user's Cart object (or a CartResponse DTO).
     */
    @GetMapping
    public ResponseEntity<Cart> getCart() {
        Long userId = getCurrentUserId();
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * POST /api/v1/cart/items
     * Adds a new item to the cart or increases the quantity of an existing item.
     *
     * @param request Contains productId and quantity.
     * @return The updated Cart object.
     */
    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemRequest request) {
        Long userId = getCurrentUserId();
        // Null checks for production code would be necessary or use @Valid
        Cart updatedCart = cartService.addToCart(
                userId,
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * PUT /api/v1/cart/items/{productId}
     * Updates the quantity of a specific item in the cart. If quantity is 0, the item is removed.
     *
     * @param productId The ID of the product to update.
     * @param request Contains the new quantity.
     * @return The updated Cart object.
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<Cart> updateItemQuantity(
            @PathVariable Long productId,
            @RequestBody CartItemRequest request) {
        Long userId = getCurrentUserId();
        Cart updatedCart = cartService.updateCartItemQuantity(
                userId,
                productId,
                request.getQuantity()
        );
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * DELETE /api/v1/cart/items/{productId}
     * Removes a specific item completely from the cart.
     *
     * @param productId The ID of the product to remove.
     * @return 204 No Content on successful removal.
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/v1/cart
     * Clears all items from the user's cart.
     *
     * @return 204 No Content on successful clear.
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        Long userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
