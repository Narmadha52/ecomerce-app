// src/main/java/com/ecommerce/backend/payment/service/PaymentService.java

package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderStatus;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.dto.PaymentOrderDto;
import com.ecommerce.backend.dto.PaymentVerificationDto;
import com.ecommerce.backend.model.Transaction;
import com.ecommerce.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID; // Used for simulation

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Inject payment gateway keys (e.g., Razorpay/Stripe keys)
    @Value("${payment.razorpay.keyId}")
    private String razorpayKeyId;

    // --- 1. Create Payment Order (Client Initiation) ---

    /**
     * Simulates creating an Order on the Payment Gateway side.
     * In a real scenario, this involves calling the PG SDK (e.g., Razorpay.createOrder()).
     * @param dto Contains order ID and amount.
     * @return The payment gateway's order ID (needed by the frontend).
     */
    public String createPaymentOrder(PaymentOrderDto dto) {
        // 1. Fetch Order to verify amount
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found."));

        // Ensure the order status is PENDING_PAYMENT and amount matches
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT || !order.getTotalAmount().equals(dto.getAmount())) {
            throw new RuntimeException("Order is not available for payment or amount mismatch.");
        }

        // 2. SIMULATE CALL TO PAYMENT GATEWAY (e.g., Razorpay/Stripe SDK)
        // This call creates a payment order on their server.
        String paymentGatewayOrderId = "pg_order_" + UUID.randomUUID().toString().substring(0, 8);

        // Return the PG Order ID, which the frontend uses to open the payment screen
        return paymentGatewayOrderId;
    }

    // --- 2. Verify Payment and Finalize Order ---

    /**
     * Verifies the payment and updates the order status.
     * @param verificationDto Details returned by the frontend after payment.
     * @return The updated Order.
     */
    @Transactional
    public Order verifyPaymentAndFinalizeOrder(PaymentVerificationDto verificationDto) {
        // 1. Fetch Order
        Order order = orderRepository.findById(verificationDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found."));

        // 2. CRYPTOGRAPHIC VERIFICATION (CRUCIAL STEP)
        // In a real application, you use the PG SDK to verify the signature/hash
        // to confirm the payment was successful and not tampered with.
        boolean isPaymentValid = verifySignature(
                verificationDto.getPaymentGatewayOrderId(),
                verificationDto.getPaymentGatewayPaymentId(),
                verificationDto.getPaymentSignature()
        );

        if (!isPaymentValid) {
            // If verification fails, update status to FAILED and throw error
            order.setStatus(OrderStatus.PENDELIVERY_FAILED); // Assuming we define a FAILED status
            orderRepository.save(order);
            throw new RuntimeException("Payment verification failed! Transaction may be fraudulent.");
        }

        // 3. Save Transaction Record
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setPaymentGatewayOrderId(verificationDto.getPaymentGatewayOrderId());
        transaction.setPaymentGatewayPaymentId(verificationDto.getPaymentGatewayPaymentId());
        transaction.setAmountPaid(order.getTotalAmount());
        transaction.setStatus("SUCCESS");
        transaction.setSignature(verificationDto.getPaymentSignature());
        transactionRepository.save(transaction);

        // 4. Update Order Status to PLACED
        order.setStatus(OrderStatus.PLACED);
        return orderRepository.save(order);
    }

    // --- Helper for Simulation ---
    private boolean verifySignature(String pgOrderId, String pgPaymentId, String signature) {
        // !!! WARNING: This is a SIMULATION. !!!
        // In a real Razorpay/Stripe integration, this method uses their SDK
        // and your secret key to cryptographically verify the signature hash.
        // For development, we return true.
        return true;
    }
}
