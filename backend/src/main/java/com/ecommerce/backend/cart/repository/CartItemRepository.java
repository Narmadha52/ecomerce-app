package com.ecommerce.backend.cart.repository;

import com.ecommerce.backend.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for CartItem entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Finds a specific CartItem by the ID of the Cart it belongs to
     * and the ID of the Product it holds. This is crucial for updating
     * quantities or checking for item existence before adding.
     *
     * Assuming the CartItem model has fields named 'cart' and 'productId',
     * Spring Data JPA will generate the appropriate query.
     *
     * @param cartId The ID of the parent Cart.
     * @param productId The ID of the Product in the item.
     * @return An Optional containing the CartItem if found.
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
