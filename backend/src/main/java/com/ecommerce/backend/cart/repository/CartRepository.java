package com.ecommerce.backend.cart.repository;

import com.ecommerce.backend.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Resolves "Cannot resolve method findByUser in CartRepository".
     * Finds the Cart associated with a specific User entity.
     */
    Optional<Cart> findByUser(User user);
}
