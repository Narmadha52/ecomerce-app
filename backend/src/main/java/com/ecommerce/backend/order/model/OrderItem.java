package com.ecommerce.backend.order.model;

import com.ecommerce.backend.product.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*; // Corrected to use Jakarta Persistence API
import java.math.BigDecimal;

/**
 * Represents a single product and its quantity within a specific Order.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship back to the parent Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many-to-one relationship to the specific Product being ordered
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Quantity of the product in this order line
    @Column(nullable = false)
    private Integer quantity;

    // The price of the product at the moment the order was placed (for historical accuracy)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrder;
}
