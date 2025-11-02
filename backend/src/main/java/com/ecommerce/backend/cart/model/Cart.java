package com.ecommerce.backend.cart.model;

// FIX: Using jakarta.persistence instead of javax.persistence for Spring Boot 3+
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user's shopping cart. This is a one-to-one relationship with the User entity.
 */
@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One relationship with the User. FetchType.LAZY is generally preferred.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // One-to-Many relationship with CartItem. If the Cart is deleted, all items must be deleted (CascadeType.ALL).
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    // Timestamp for last update
    private LocalDateTime lastModified;

    // --- Lifecycle Callbacks ---

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }

    // --- Utility Methods ---

    /**
     * Adds a CartItem to the collection and links it back to this Cart.
     */
    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this);
    }

    /**
     * Removes a CartItem from the collection and unlinks it from this Cart.
     */
    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
    }
}
