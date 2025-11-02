// src/main/java/com/ecommerce/backend/payment/model/Transaction.java

package com.ecommerce.backend.payment.model;

import com.ecommerce.backend.order.model.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link the transaction to the Order it paid for
    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    @Column(nullable = false)
    private String paymentGatewayOrderId; // ID received from Razorpay/Stripe

    private String paymentGatewayPaymentId; // ID received upon successful payment

    @Column(nullable = false)
    private BigDecimal amountPaid;

    @Column(nullable = false)
    private String currency = "INR"; // Or USD, etc.

    @Column(nullable = false)
    private String status; // E.g., SUCCESS, FAILED, PENDING

    private LocalDateTime transactionDate = LocalDateTime.now();

    // Often required for verification (e.g., Razorpay signature)
    private String signature;
}