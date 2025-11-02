package com.ecommerce.backend.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a customer order in the e-commerce system.
 * This entity tracks order details, associated user, status, and total amount.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with the User who placed the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    // The current status of the order (e.g., PENDING, SHIPPED, DELIVERED)
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    // The total amount of the order, including tax and shipping
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Shipping information
    @Column(nullable = false)
    private String shippingAddress;

    // One-to-many relationship with the individual items in the order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();
}
